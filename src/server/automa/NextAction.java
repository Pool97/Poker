package server.automa;

import interfaces.PokerState;
import interfaces.TransitionStrategy;
import server.model.PlayerModel;
import server.model.Position;
import server.model.Room;

public class NextAction extends Action implements PokerState {
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";
    private TransitionStrategy strategy;
    private int roundNumber;

    public NextAction(MatchHandler match) {
        super(match);
    }

    @Override
    public void goNext() {
        Room room = match.getRoom();
        Position nextPosition = Position.SB;

        while (!((nextPosition == Position.SB && roundNumber >= 1 && (checkIfOnePlayerRemained() || isEquityReached()) || (nextPosition != Position.SB && checkIfOnePlayerRemained())))) {
            PlayerModel player = room.getPlayer(nextPosition);
            if (!player.hasFolded() && !player.isAllIn()) {
                doAction(player);
            }
            nextPosition = room.getNextPosition(nextPosition);

            if (nextPosition == Position.SB)
                roundNumber++;
        }


        if (checkIfOnePlayerRemained()) {
            MatchHandler.logger.info(ONE_PLAYER_ONLY);
            strategy.makeTransition();
        } else if (isEquityReached()) {
            MatchHandler.logger.info(EQUITY_REACHED);
            strategy.makeTransition();
        }
    }


    public void setTransitionStrategy(TransitionStrategy strategy) {
        this.strategy = strategy;
    }
}
