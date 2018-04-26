package server.automa;

import events.BigBlindEvent;
import events.Events;
import events.SmallBlindEvent;
import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import interfaces.StateSwitcher;
import server.model.DeckModel;
import server.model.MatchModel;
import server.model.Room;
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
    private MatchModel matchModel;
    private DeckModel deckModel;
    private ServerManager connectionHandler;
    private ArrayList<Observer> observers;
    private Events stateChanges;

    /**
     * Costruttore della classe StartTurn.
     * @param matchModel Modello del match
     * @param connectionHandler Gestore della connessione
     */

    public StartTurn(MatchModel matchModel, ServerManager connectionHandler) {
        observers = new ArrayList<>();
        stateChanges = new Events();
        deckModel = new DeckModel();
        this.matchModel = matchModel;
        this.connectionHandler = connectionHandler;

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
        deckModel.createAndShuffle();
        matchModel.resetPot();
        matchModel.increaseBlinds();
        Room room = connectionHandler.getRoom();
        room.getPlayers().forEach(player -> player.setTotalChips(matchModel.getStartingChipAmount()));
        stateChanges.addEvent(new BigBlindEvent(matchModel.getBigBlind()));
        stateChanges.addEvent(new SmallBlindEvent(matchModel.getSmallBlind()));
        connectionHandler.sendMessage(room.getConnections(), new CountDownLatch(1), stateChanges);
        notifyObservers();
    }

    /**
     * {@link PokerState#accept(StateSwitcher)}
     */

    @Override
    public void accept(StateSwitcher switcher) {
        switcher.switchState(this);
    }
}
