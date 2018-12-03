package server.model.automa;

import interfaces.TransitionStrategy;
import server.model.PlayerModel;
import server.model.Position;

import java.util.ListIterator;

public class NextBettingRound extends BettingRound{
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";
    private TransitionStrategy strategy;
    private int roundNumber;

    public NextBettingRound() {
        super();
    }

    @Override
    public void goNext(Game game) {
        int nextPosition = Position.SB.ordinal();

        ListIterator<PlayerModel> iterator = table.iterator();
        PlayerModel player;

        while (!turnFinished(nextPosition)) {

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
            strategy.makeTransition();
        }
    }

    @Override
    protected boolean turnFinished(int cursor) {
        return (cursor == Position.SB.ordinal() && ((isMatched() && roundNumber >= 1) || table.countPlayersInGame() == 1 || ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) && !checkForActingPlayer()) || table.isAllPlayersAllIn()))
                || (cursor != Position.SB.ordinal()) && (table.countPlayersInGame() == 1 || ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) && !checkForActingPlayer()) || table.isAllPlayersAllIn());
    }

    public void setTransitionStrategy(TransitionStrategy strategy) {
        this.strategy = strategy;
    }
}
