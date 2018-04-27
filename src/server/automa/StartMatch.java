package server.automa;

import events.Events;
import events.PlayerAddedEvent;
import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import interfaces.StateSwitcher;
import server.model.Room;
import server.model.TurnModel;
import server.socket.ServerManager;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * StartMatch è il primo stato dell'automa.
 *
 * In questo stato devono essere compiute le seguenti azioni:
 * - Stabilire le posizioni iniziali dei giocatori ({@link server.model.Position});
 * - Informare tutti i giocatori dei rispettivi avversari
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class StartMatch implements PokerState, Observable {
    private ServerManager connectionHandler;
    private ArrayList<Observer> observers;
    private TurnModel turnModel;

    /**
     * Costruttore della classe StartMatch.
     * @param turnModel Modello del match
     * @param connectionHandler Gestore della connessione.
     */

    public StartMatch(TurnModel turnModel, ServerManager connectionHandler) {
        observers = new ArrayList<>();
        this.connectionHandler = connectionHandler;
        this.turnModel = turnModel;
    }

    /**
     * Vedi {@link PokerState#start()}
     */

    @Override
    public void start() {
        Room room = connectionHandler.getRoom();
        room.setInitialPositions();
        CountDownLatch countdown = new CountDownLatch(1);
        Events startEvents = new Events();
        turnModel.setStartingChipAmount(connectionHandler.getRoom().getSize() * 10000);
        room.getPlayers().forEach(player -> startEvents.addEvent(new PlayerAddedEvent(player.getNickname(), player.getAvatarFilename(), player.getPosition(),
                player.getTotalChips())));
        connectionHandler.sendMessage(room.getConnections(), countdown, startEvents);

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
