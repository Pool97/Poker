package server.model.automa;

import server.model.PlayerModel;
import server.model.Position;

/**
 * PokerAction è lo stato che gestisce un giro di puntate in un determinato turno. È bene ricordare che nel corso di un
 * turno ci possono essere da 1 a 4 giri di puntate, in base all'evoluzione del turno e alla scelta dei
 * giocatori di rimanere in gioco oppure di foldare.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class FirstBettingRound extends BettingRound {
    private final static String START_ACTIONS = "Inizio il giro di puntate non obbligatorie... \n";
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";

    public FirstBettingRound() {
        super();
    }

    @Override
    public void goNext(Context context) {
        Context.logger.info(START_ACTIONS);
        Position nextPosition = table.getNextPosition(Position.BB);

        while (!turnFinished(nextPosition)) {
            PlayerModel player = table.getPlayer(nextPosition);
            if (!player.hasFolded() && !player.isAllIn()) {
                doAction(player);
            }
            nextPosition = table.getNextPosition(nextPosition);
        }

        if (playersAnalyzer.countPlayersAtStake() == 1) {
            Context.logger.info(ONE_PLAYER_ONLY);
            context.setState(new Showdown());
        } else if ((playersAnalyzer.countActivePlayers() == 1 && playersAnalyzer.countAllInPlayers() > 0) || isMatched() || playersAnalyzer.isAllPlayersAtStakeAllIn()) {
            context.setState(new Flop());
        }
    }

    protected boolean turnFinished(Position nextPosition) {
        return (nextPosition == Position.SB && isMatched())
                || (nextPosition != Position.SB) && (playersAnalyzer.countPlayersAtStake() == 1 ||
                ((playersAnalyzer.countActivePlayers() == 1 && playersAnalyzer.countAllInPlayers() > 0) && !checkForActingPlayer())
                || playersAnalyzer.isAllPlayersAtStakeAllIn());
    }
}
