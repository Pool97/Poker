package server.model.automa;

import server.model.PlayerModel;
import server.model.Position;

import java.util.AbstractMap;
import java.util.ListIterator;

public class FirstNoLimitRound extends NoLimitBettingRound {
    private final static String START_ACTIONS = "Inizio il giro di puntate non obbligatorie... \n";
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";

    @Override
    public void goNext(Game game) {
        Game.logger.info(START_ACTIONS);
        int nextPosition = (Position.BB.ordinal() + 1) % table.size();

        dealer.setMinimumLegalRaise(new AbstractMap.SimpleEntry<>(
                table.iterator(Position.SB.ordinal()).next().getNickname(), game.getBigBlind()));

        ListIterator<PlayerModel> iterator = nextPosition == 0 ? table.iterator() : table.iterator(Position.BB.ordinal());
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
            }

        }

        if (table.countPlayersInGame() == 1) {
            Game.logger.info(ONE_PLAYER_ONLY);
            game.setState(new Showdown());
        } else if ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) || isMatched() || table.isAllPlayersAllIn()) {
            game.setState(new Flop());
        }
    }

    protected boolean roundFinished(int cursor) {
        return (cursor == Position.SB.ordinal() && isMatched())
                || (cursor != Position.SB.ordinal()) && (table.countPlayersInGame() == 1 ||
                ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) && !checkForActingPlayer())
                || table.isAllPlayersAllIn());
    }
}
