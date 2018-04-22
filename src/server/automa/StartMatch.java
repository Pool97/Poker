package server.automa;

import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import interfaces.StateSwitcher;
import server.messages.PlayersMessage;
import server.model.Room;
import server.socket.ServerManager;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * StartMatch è il primo stato dell'automa.
 * Il suo compito è quello di gestire la creazione della stanza per permettere ai Players di
 * sintonizzarsi e informarli che la partita sta per iniziare.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class StartMatch implements PokerState, Observable {
    private ServerManager connectionHandler;
    private ArrayList<Observer> observers;

    /**
     * Costruttore della classe StartMatch.
     *
     * @param connectionHandler Gestore della connessione.
     */

    public StartMatch(ServerManager connectionHandler) {
        observers = new ArrayList<>();
        this.connectionHandler = connectionHandler;
    }

    /**
     * Vedi anche {@link PokerState#start()}
     * <p>
     * Vengono informati tutti i Players dei relativi avversari.
     * StartMatch non avverte il gestore dell'automa finchè non si è assicurato che tutti i messaggi
     * siano arrivati a destinazione.
     */

    @Override
    public void start() {
        Room room = connectionHandler.getRoom();
        CountDownLatch countdown = new CountDownLatch(1);
        connectionHandler.sendMessage(room.getConnections(), countdown, new PlayersMessage(room.getPlayers()));
        try {
            countdown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            notifyObservers();
        }
    }

    /**
     * Vedi {@link PokerState#accept(StateSwitcher)}
     */

    @Override
    public void accept(StateSwitcher switcher) {
        switcher.switchState(this);
    }

    /**
     * Vedi {@link Observable#attach(Observer)}
     */

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * Vedi {@link Observable#detach(Observer)}
     */

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Vedi {@link Observable#notifyObservers()}
     */

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }
}
