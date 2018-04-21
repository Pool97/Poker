package server;

import server.interfaces.Observable;
import server.interfaces.Observer;
import server.model.MatchModel;
import server.socket.ServerSocketManager;
import server.states.StakePhase;
import server.states.StartMatch;
import server.states.StartTurn;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * Gestore dell'automa a stati finiti. Permette di effettuare le varie transizioni da uno stato all'altro attraverso
 * il pattern Observer. Ogni stato, appena si conclude, notifica allo StateManager che può effettuare la transizione
 * allo stato successivo. Non è necessario mantenere in memoria tutti gli stati di ogni turno perchè i principali avvenimenti
 * della partita vengono registrati e mantenuti consistenti nel Model.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class StateManager implements Observer {
    private MatchModel matchModel;
    private static Logger logger = Logger.getLogger(ServerSocketManager.class.getName());
    private final static String PLAYERS_CONNECTED_INFO = "SERVER -> TUTTI I GIOCATORI SONO CONNESSI, LA PARTITA PUÒ INIZIARE. \n";

    public StateManager() throws InterruptedException {
        matchModel = new MatchModel();

        CountDownLatch waitingForNumberPlayers = new CountDownLatch(1);
        ServerSocketManager connectionHandler = new ServerSocketManager(waitingForNumberPlayers);
        new Thread(connectionHandler).start();
        waitingForNumberPlayers.await();

        int playerNumbers = connectionHandler.getTotalPlayers();
        CountDownLatch waitingPlayers = new CountDownLatch(playerNumbers);
        connectionHandler.setCountdownForClients(waitingPlayers);

        try {
            waitingPlayers.await();
            logger.info(PLAYERS_CONNECTED_INFO);
            StartMatch startMatch = new StartMatch(matchModel);
            startMatch.attach(this);
            startMatch.notifyAllPlayers(connectionHandler.getAllClientsConnection());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable) {

        if(observable instanceof StartMatch) {
            StartTurn startTurn = new StartTurn(matchModel);
            startTurn.attach(this);
        }

        if(observable instanceof StartTurn)
            new StakePhase();

        //if(observable instanceof StakePhase)
            //new FlopPhase();
    }

    public static void main(String [] args) throws InterruptedException {
        StateManager manager = new StateManager();
    }
}
