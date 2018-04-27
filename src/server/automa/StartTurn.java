package server.automa;

import events.BlindsUpdatedEvent;
import events.Events;
import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import interfaces.StateSwitcher;
import server.model.DeckModel;
import server.model.Room;
import server.model.TurnModel;
import server.socket.ServerManager;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * StartTurn è il secondo stato dell'automa.
 * Esso può iniziare per via di due transizioni:
 * - Transizione da StartMatch: StartTurn rappresenta il primo turno della partita di Poker.
 * - Transizione da ShowdownState: StartTurn rappresenta un nuovo turno della partita di Poker, subito
 *   dopo la conclusione del precedente, conclusosi con lo stato di Showdown.
 *
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

public class StartTurn implements PokerState, Observable {
    private TurnModel turnModel;
    private DeckModel deckModel;
    private ServerManager serverHandler;
    private ArrayList<Observer> observers;
    private Events stateChanges;

    /**
     * Costruttore della classe StartTurn.
     * @param turnModel Modello del match
     * @param serverHandler Gestore della connessione
     */

    public StartTurn(TurnModel turnModel, ServerManager serverHandler) {
        observers = new ArrayList<>();
        stateChanges = new Events();
        deckModel = new DeckModel();
        this.turnModel = turnModel;
        this.serverHandler = serverHandler;

    }

    /**
     * {@link Observable#attach(Observer)}
     */

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * {@link Observable#detach(Observer)}
     */

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * {@link Observable#notifyObservers()}
     */

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }

    /**
     * Vedi {@link PokerState#start()}
     */

    @Override
    public void start() {
        serverHandler.logger.info("Start Turn è iniziato. \n");
        deckModel.createAndShuffle();
        turnModel.resetPot();
        turnModel.increaseBlinds();
        Room room = serverHandler.getRoom();
        room.getPlayers().forEach(player -> player.setTotalChips(turnModel.getStartingChipAmount()));
        stateChanges.addEvent(new BlindsUpdatedEvent(turnModel.getSmallBlind(), turnModel.getBigBlind()));
        serverHandler.logger.info("Fornisco ai players i parametri aggiornati per il nuovo turno... \n");
        serverHandler.sendMessage(room.getConnections(), new CountDownLatch(1), stateChanges);
        notifyObservers();
        serverHandler.logger.info("Start Turn è finito. \n");
    }

    /**
     * {@link PokerState#accept(StateSwitcher)}
     */

    @Override
    public void accept(StateSwitcher switcher) {
        switcher.switchState(this);
    }
}
