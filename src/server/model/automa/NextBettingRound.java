package server.model.automa;

import interfaces.TransitionStrategy;
import server.model.PlayerModel;
import server.model.Position;

public class NextBettingRound extends BettingRound{
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";
    private TransitionStrategy strategy;
    private int roundNumber;

    public NextBettingRound() {
        super();
    }

    @Override
    public void goNext(Context context) {
        Position nextPosition = Position.SB;

        while (!turnFinished(nextPosition)) {
            PlayerModel player = table.getPlayer(nextPosition);
            if (!player.hasFolded() && !player.isAllIn()) {
                doAction(player);
            }
            nextPosition = table.getNextPosition(nextPosition);
            if (nextPosition == Position.SB)
                roundNumber++;
        }

        if (playersAnalyzer.countPlayersAtStake() == 1) {
            Context.logger.info(ONE_PLAYER_ONLY);
            context.setState(new Showdown());
        } else if (isMatched() || (playersAnalyzer.countActivePlayers() == 1 && playersAnalyzer.countAllInPlayers() > 0)
                || playersAnalyzer.isAllPlayersAtStakeAllIn()) {
            Context.logger.info(EQUITY_REACHED);
            strategy.makeTransition();
        }
    }

    @Override
    protected boolean turnFinished(Position nextPosition) {
        return (nextPosition == Position.SB && ((isMatched() && roundNumber >= 1) || playersAnalyzer.countPlayersAtStake() == 1 || ((playersAnalyzer.countActivePlayers() == 1 && playersAnalyzer.countAllInPlayers() > 0) && !checkForActingPlayer()) || playersAnalyzer.isAllPlayersAtStakeAllIn()))
                || (nextPosition != Position.SB) && (playersAnalyzer.countPlayersAtStake() == 1 || ((playersAnalyzer.countActivePlayers() == 1 && playersAnalyzer.countAllInPlayers() > 0) && !checkForActingPlayer()) || playersAnalyzer.isAllPlayersAtStakeAllIn());
    }

    public void setTransitionStrategy(TransitionStrategy strategy) {
        this.strategy = strategy;
    }
}
