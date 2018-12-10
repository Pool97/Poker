package server.model.automa.round;

import interfaces.PokerState;
import interfaces.TransitionStrategy;
import server.controller.Game;
import server.model.PlayerModel;
import server.model.Position;
import server.model.automa.Showdown;

import java.util.AbstractMap;
import java.util.ListIterator;

public class NextNoLimitRound extends NoLimitRound {
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";
    private TransitionStrategy<PokerState> strategy;
    private int roundNumber;

    public NextNoLimitRound() {
        super();
    }

    @Override
    public void goNext(Game game) {
        int nextPosition = Position.SB.ordinal();

        dealer.setMinimumLegalRaise(new AbstractMap.SimpleEntry<>("", game.getBigBlind()));
        ListIterator<PlayerModel> iterator = table.iterator();
        PlayerModel player;

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

        }

        if (table.countPlayersInGame() == 1) {
            Game.logger.info(ONE_PLAYER_ONLY);
            game.setState(new Showdown());
        } else if (isMatched() || (table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0)
                || table.isAllPlayersAllIn()) {
            Game.logger.info(EQUITY_REACHED);
            game.setState(strategy.determineTransition());
        }
    }

    @Override
    protected boolean roundFinished(int cursor) {
        return (cursor == Position.SB.ordinal() && ((isMatched() && roundNumber >= 1) || table.countPlayersInGame() == 1 || ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) && !checkForActingPlayer()) || table.isAllPlayersAllIn()))
                || (cursor != Position.SB.ordinal()) && (table.countPlayersInGame() == 1 || ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) && !checkForActingPlayer()) || table.isAllPlayersAllIn());
    }

    public void setTransitionStrategy(TransitionStrategy<PokerState> strategy) {
        this.strategy = strategy;
    }
}
