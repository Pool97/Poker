package server.automa;

import interfaces.PokerState;
import interfaces.TransitionStrategy;
import server.model.PlayerModel;
import server.model.Position;
import server.model.Room;

public class NextBettingRound extends BettingRound implements PokerState {
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";
    private TransitionStrategy strategy;
    private int roundNumber;

    public NextBettingRound(MatchHandler match) {
        super(match);
    }

    @Override
    public void goNext() {
        Room room = match.getRoom();
        Position nextPosition = Position.SB;

        while (!turnFinishedOtherVersion(nextPosition, roundNumber)) {
            PlayerModel player = room.getPlayer(nextPosition);
            if (!player.hasFolded() && !player.isAllIn()) {
                doAction(player);
            }
            nextPosition = room.getNextPosition(nextPosition);
            if (nextPosition == Position.SB)
                roundNumber++;
        }

        if (playersAnalyzer.countPlayersAtStake() == 1) {
            MatchHandler.logger.info(ONE_PLAYER_ONLY);
            match.setState(new Showdown(match));
        } else if (isMatched() || (playersAnalyzer.countActivePlayers() == 1 && playersAnalyzer.countAllInPlayers() > 0) || playersAnalyzer.isAllPlayersAtStakeAllIn()) {
            System.out.println("Dio bambinisissmo");
            MatchHandler.logger.info(EQUITY_REACHED);
            strategy.makeTransition();
        }
    }


    public boolean turnFinishedOtherVersion(Position nextPosition, int roundNumber) {
        return (nextPosition == Position.SB && ((isMatched() && roundNumber >= 1) || playersAnalyzer.countPlayersAtStake() == 1 || ((playersAnalyzer.countActivePlayers() == 1 && playersAnalyzer.countAllInPlayers() > 0) && !checkForActingPlayer()) || playersAnalyzer.isAllPlayersAtStakeAllIn()))
                || (nextPosition != Position.SB) && (playersAnalyzer.countPlayersAtStake() == 1 || ((playersAnalyzer.countActivePlayers() == 1 && playersAnalyzer.countAllInPlayers() > 0) && !checkForActingPlayer()) || playersAnalyzer.isAllPlayersAtStakeAllIn());
    }

    public void setTransitionStrategy(TransitionStrategy strategy) {
        this.strategy = strategy;
    }
}
