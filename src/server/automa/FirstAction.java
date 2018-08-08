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

public class FirstAction extends Action implements PokerState {
    //private final static String STATE_STARTED = "Lo stato di PokerAction è avviato. \n";
    private final static String START_ACTIONS = "Inizio il giro di puntate non obbligatorie... \n";
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore nel giro di puntate! \n";
    private final static String EQUITY_REACHED = "La puntata massima è stata pareggiata! \n";


    /**
     * Costruttore della classe PokerAction
     *
     * @param match Gestore dell'automa
     */

    public FirstAction(MatchHandler match) {
        super(match);
    }

    @Override
    public void goNext() {
        //MatchHandler.logger.info(STATE_STARTED);
        MatchHandler.logger.info(START_ACTIONS);
        Room room = match.getRoom();
        Position nextPosition = room.getNextPosition(Position.BB);

        while (!((nextPosition == Position.SB) && (checkIfOnePlayerRemained() || (isEquityReached())))) {
            PlayerModel player = room.getPlayer(nextPosition);
            if (!player.hasFolded() && !player.isAllIn() && !player.hasLost()) {
                doAction(player);
            }
            nextPosition = room.getNextPosition(nextPosition);
        }

        if (checkIfOnePlayerRemained()) {
            MatchHandler.logger.info(ONE_PLAYER_ONLY);
            match.setState(new TurnEnd(match));
        } else if (isEquityReached()) {
            MatchHandler.logger.info(EQUITY_REACHED);
            match.setState(new Flop(match));
        }
    }
}
