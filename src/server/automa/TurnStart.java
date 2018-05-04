package server.automa;

import events.BlindsUpdatedEvent;
import events.Events;
import interfaces.PokerState;
import server.model.MatchModel;
import server.model.Room;

/**
 * TurnStart è il secondo stato dell'automa.
 * Esso può iniziare per via di due transizioni:
 * - Transizione da StartGame: TurnStart rappresenta il primo turno della partita di Poker.
 * - Transizione da Showdown: TurnStart rappresenta un nuovo turno della partita di Poker, subito
 * dopo la conclusione del precedente, conclusosi con lo stato di Showdown.
 * <p>
 * In questo stato devono essere compiute le seguenti azioni:
 * - creazione e mescolamento del mazzo di carte;
 * - riazzeramento del pot (per evitare di continuare a utilizzare il pot del turno precedente)
 * - incremento dei bui
 * - Informare tutti i players dell'aggiornamento del valore dei bui
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class TurnStart implements PokerState {
    private final static String STATE_STARTED = "Lo stato di TurnStart è avviato. \n";
    private final static String CONF_TURN = "Configurazione dei parametri del turno in corso... \n";
    private final static String CONF_ENDED = "Fornisco ai players i parametri aggiornati per il nuovo turno... \n";
    private MatchHandler match;

    /**
     * Costruttore della classe TurnStart.
     */

    public TurnStart(MatchHandler match) {
        this.match = match;
    }

    /**
     * {@link PokerState#goNext()}
     */

    @Override
    public void goNext() {
        MatchHandler.logger.info(STATE_STARTED);
        MatchHandler.logger.info(CONF_TURN);
        MatchModel matchModel = match.getMatchModel();
        Room room = match.getRoom();

        match.getTurnModel().createDeck();
        match.getTurnModel().resetPot();
        matchModel.increaseBlinds();

        MatchHandler.logger.info(CONF_ENDED);
        room.sendBroadcast(new Events(new BlindsUpdatedEvent(matchModel.getSmallBlind(), matchModel.getBigBlind())));
        match.setState(new Blinds(match));
    }
}
