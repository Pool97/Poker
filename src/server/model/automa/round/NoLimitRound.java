package server.model.automa.round;

import server.controller.Game;
import server.model.PlayerModel;
import server.model.Position;
import server.model.automa.Showdown;

import java.util.AbstractMap;
import java.util.ListIterator;

public class NoLimitRound extends AbstractNoLimitRound {
    private final static String START_ACTIONS = "Inizio il giro di puntate non obbligatorie... \n";
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";
    private int nextPosition;

    public NoLimitRound(int nextPosition, int previousPosition, int betRound) {
        this.nextPosition = nextPosition;
        lastRaiseOrBetCursor = previousPosition;
        if (table.countPlayersInGame() == 2 && betRound != 0)
            roundNumber = 1;
    }

    public NoLimitRound(int nextPosition) {
        lastRaiseOrBetCursor = nextPosition;
    }

    @Override
    public void goNext(Game game) {
        Game.logger.info(START_ACTIONS);

        dealer.setMinimumLegalRaise(new AbstractMap.SimpleEntry<>(
                table.iterator(Position.SB.ordinal()).next().getNickname(), game.getBigBlind()));

        ListIterator<PlayerModel> iterator = nextPosition == 0 ? table.iterator() : table.iterator(Position.BB.ordinal());

        PlayerModel player;
        System.out.println("Last: " + lastRaiseOrBetCursor);
        while (!roundFinished(nextPosition)) {

            player = iterator.next();
            if (!player.hasFolded() && !player.isAllIn()) {
                doAction(player, game);
            }

            nextPosition = iterator.nextIndex();

            if(!iterator.hasNext()){
                iterator = table.iterator();
                nextPosition = Position.SB.ordinal();
                roundNumber++;
            }
            System.out.println("Last: " + lastRaiseOrBetCursor);
        }

        if (table.countPlayersInGame() == 1) {
            Game.logger.info(ONE_PLAYER_ONLY);
            game.setNextState(new Showdown());
        } else if ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) || isMatched() || table.isAllPlayersAllIn()) {
            game.setNextState(strategy.determineTransition());
        }
    }
}
