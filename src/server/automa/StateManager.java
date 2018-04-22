package server.automa;

import interfaces.Observer;
import interfaces.PokerState;
import server.model.MatchModel;
import server.socket.ServerManager;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * Si supponga che il match di Poker sia rappresentato logicamente da un automa a stati finiti.
 * StateManager rappresenta in termini OOP il gestore dell'automa a stati finiti.
 * Esso permette di temporizzare gli stati e decidere autonomamente quando è il momento di effettuare una transizione da uno stato all'altro
 * attraverso il pattern Observer. Ogni stato, appena si conclude, notifica allo StateManager che può effettuare la transizione
 * allo stato successivo. Inoltre, non è necessario mantenere in memoria tutti gli stati di ogni turno perchè i principali avvenimenti
 * della partita vengono registrati e mantenuti consistenti nel Model.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class StateManager implements Observer {
    private static Logger logger = Logger.getLogger(ServerManager.class.getName());
    private MatchModel matchModel;
    private ServerManager connectionHandler;
    private Switcher switcher;


    public StateManager() {
        matchModel = new MatchModel();
    }

    /**
     * Permette di avviare il Server di gioco e avviare la transizione a {@link StartMatch}, il primo
     * stato dell'automa.
     */

    public void startServer() {
        CountDownLatch roomCreationSignal = new CountDownLatch(1);
        connectionHandler = new ServerManager(roomCreationSignal);
        new Thread(connectionHandler).start();

        try {
            roomCreationSignal.await();
            switcher = new Switcher(matchModel, connectionHandler);
            StartMatch startMatch = new StartMatch(connectionHandler);
            startMatch.attach(this);
            startMatch.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * È il metodo che rende lo StateManager un vero e proprio temporizzatore.
     * Infatti, il metodo viene invocato ogni qualvolta uno stato dell'automa termina.
     * Una volta invocato, viene determinato il prossimo stato attraverso {@link Switcher}, il
     * componente cardine di questa classe.
     *
     * @param state Lo stato appena conclusosi che ha invocato questo metodo.
     */

    @Override
    public void update(PokerState state) {
        state.accept(switcher);
    }
}
