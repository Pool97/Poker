package server.automa;

import events.BlindsUpdatedEvent;
import events.Events;
import interfaces.PokerState;
import server.model.MatchModel;
import server.model.Room;
import server.socket.ServerManager;

import java.util.concurrent.CountDownLatch;

/**
 * TurnStart è il secondo stato dell'automa.
 * Esso può iniziare per via di due transizioni:
 * - Transizione da MatchStart: TurnStart rappresenta il primo turno della partita di Poker.
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
    private Match match;

    /**
     * Costruttore della classe TurnStart.
     */

    public TurnStart(Match match) {
        this.match = match;
    }

    /**
     * Vedi {@link PokerState#goNext()}
     */

    @Override
    public void goNext() {
        MatchModel matchModel = match.getMatchModel();
        ServerManager.logger.info("Start Turn è iniziato. \n");
        match.getTurnModel().createDeck();
        match.getTurnModel().resetPot();
        matchModel.increaseBlinds();
        Room room = match.getRoom();
        room.setPlayersChips(matchModel.getStartChips());
        ServerManager.logger.info("Fornisco ai players i parametri aggiornati per il nuovo turno... \n");
        room.sendBroadcast(new CountDownLatch(1),
                new Events(new BlindsUpdatedEvent(matchModel.getSmallBlind(), matchModel.getBigBlind())));
        match.setState(new Blinds(match));
        ServerManager.logger.info("Start Turn è finito. \n");
    }
}
