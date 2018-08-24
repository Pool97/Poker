package server.automa;

import interfaces.PokerState;
import server.model.MatchModel;
import server.model.Room;
import server.model.TurnModel;
import server.socket.ServerManager;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * Si supponga che il match di Poker sia rappresentato logicamente da un automa a stati finiti.
 * MatchHandler rappresenta in termini OOP il gestore dell'automa a stati finiti.
 * Esso permette di gestire la transizione degli stati. Il tipo di transizione viene decisa dallo stato stesso in
 * base a determinati parametri in ingresso, se forniti. Inoltre, non è necessario mantenere in memoria tutti gli stati
 * di ogni turno perchè i principali avvenimenti della partita vengono registrati e mantenuti consistenti nel Model.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class MatchHandler {
    private MatchModel matchModel;
    private TurnModel turnModel;
    private Room room;
    private PokerState currentState;

    public static final Logger logger = Logger.getLogger(MatchHandler.class.getName());

    /**
     * Costruttore vuoto della classe MatchHandler.
     */

    public MatchHandler() {
        matchModel = new MatchModel();
        turnModel = new TurnModel();
        room = new Room();
    }

    /**
     * Permette di impostare il nuovo stato come stato attuale dell'automa.
     *
     * @param state Nuovo stato
     */

    public void setState(PokerState state) {
        this.currentState = state;
    }

    /**
     * Permette di avviare il nuovo stato.
     */

    public void start() {
        currentState.goNext();
    }

    /**
     * Permette di avviare il Server di gioco e impostare come stato {@link StartGame}, il primo
     * stato dell'automa.
     */

    public void startServer() {
        CountDownLatch roomCreationSignal = new CountDownLatch(1);
        ServerManager serverManager = new ServerManager(room, roomCreationSignal);
        new Thread(serverManager).start();
        try {
            roomCreationSignal.await();
            setState(new StartGame(this));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setInitialParameters(int startChips) {
        matchModel.setStartChips(startChips);
        matchModel.setInitialBlinds();
    }

    TurnModel getTurnModel() {
        return turnModel;
    }

    MatchModel getMatchModel() {
        return matchModel;
    }

    Room getRoom() {
        return room;
    }
}
