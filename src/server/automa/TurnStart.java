package server.automa;

import events.BlindsUpdatedEvent;
import events.Events;
import interfaces.PokerState;
import server.model.DeckModel;
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
    private DeckModel deckModel;
    private Events stateChanges;
    private Match match;

    /**
     * Costruttore della classe TurnStart.
     */

    public TurnStart(Match match) {
        stateChanges = new Events();
        deckModel = new DeckModel();
        this.match = match;
    }

    /**
     * Vedi {@link PokerState#goNext()}
     */

    @Override
    public void goNext() {
        ServerManager serverManager = match.getServerManager();
        MatchModel matchModel = match.getMatchModel();
        serverManager.logger.info("Start Turn è iniziato. \n");
        deckModel.createAndShuffle();
        match.getTurnModel().resetPot();
        matchModel.increaseBlinds();
        Room room = serverManager.getRoom();
        room.getPlayers().forEach(player -> player.setTotalChips(matchModel.getStartChips()));
        stateChanges.addEvent(new BlindsUpdatedEvent(matchModel.getSmallBlind(), matchModel.getBigBlind()));
        serverManager.logger.info("Fornisco ai players i parametri aggiornati per il nuovo turno... \n");
        serverManager.sendMessage(room.getConnections(), new CountDownLatch(1), stateChanges);
        match.setState(new Blinds(match));
        serverManager.logger.info("Start Turn è finito. \n");
    }
}
