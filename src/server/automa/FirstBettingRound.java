package server.automa;

import interfaces.PokerState;
import server.model.PlayerModel;
import server.model.Position;
import server.model.Room;

/**
 * PokerAction è lo stato che gestisce un giro di puntate in un determinato turno. È bene ricordare che nel corso di un
 * turno ci possono essere da 1 a 4 giri di puntate, in base all'evoluzione del turno e alla scelta dei
 * giocatori di rimanere in gioco oppure di foldare.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class FirstBettingRound extends BettingRound implements PokerState {
    //private final static String STATE_STARTED = "Lo stato di PokerAction è avviato. \n";
    private final static String START_ACTIONS = "Inizio il giro di puntate non obbligatorie... \n";
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";


    /**
     * Costruttore della classe PokerAction
     *
     * @param match Gestore dell'automa
     */

    public FirstBettingRound(MatchHandler match) {
        super(match);
    }

    @Override
    public void goNext() {
        //MatchHandler.logger.info(STATE_STARTED);
        MatchHandler.logger.info(START_ACTIONS);
        Room room = match.getRoom();
        Position nextPosition = room.getNextPosition(Position.BB);

        while (!turnFinished(nextPosition)) {
            PlayerModel player = room.getPlayer(nextPosition);
            if (!player.hasFolded() && !player.isAllIn() && !player.hasLost()) {
                doAction(player);
            }
            nextPosition = room.getNextPosition(nextPosition);
        }

        if (playersAnalyzer.countPlayersAtStake() == 1) {
            MatchHandler.logger.info(ONE_PLAYER_ONLY);
            match.setState(new Showdown(match));
        } else if ((playersAnalyzer.countActivePlayers() == 1 && playersAnalyzer.countAllInPlayers() > 0) || isMatched() || playersAnalyzer.isAllPlayersAtStakeAllIn()) {
            match.setState(new Flop(match));
        }
    }

    private boolean turnFinished(Position nextPosition) {
        return (nextPosition == Position.SB && isMatched())
                || (nextPosition != Position.SB) && (playersAnalyzer.countPlayersAtStake() == 1 || ((playersAnalyzer.countActivePlayers() == 1 && playersAnalyzer.countAllInPlayers() > 0) && !checkForActingPlayer()) || playersAnalyzer.isAllPlayersAtStakeAllIn());
    }
}
