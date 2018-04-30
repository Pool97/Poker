package server.automa;

import interfaces.PokerState;
import server.model.MatchModel;
import server.model.TurnModel;
import server.socket.ServerManager;

import java.util.concurrent.CountDownLatch;

/**
 * Si supponga che il match di Poker sia rappresentato logicamente da un automa a stati finiti.
 * Match rappresenta in termini OOP il gestore dell'automa a stati finiti.
 * Esso permette di temporizzare gli stati e decidere autonomamente quando è il momento di effettuare una transizione da uno stato all'altro
 * attraverso il pattern Observer. Ogni stato, appena si conclude, notifica allo Match che può effettuare la transizione
 * allo stato successivo. Inoltre, non è necessario mantenere in memoria tutti gli stati di ogni turno perchè i principali avvenimenti
 * della partita vengono registrati e mantenuti consistenti nel Model.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Match {
    private MatchModel matchModel;
    private TurnModel turnModel;
    private ServerManager serverManager;
    private PokerState currentState;


    public Match() {
        matchModel = new MatchModel();
        turnModel = new TurnModel();
    }


    public void setState(PokerState state) {
        this.currentState = state;
    }

    public void start() {
        currentState.goNext();
    }

    /**
     * È il metodo che rende lo Match un vero e proprio temporizzatore.
     * Infatti, il metodo viene invocato ogni qualvolta uno stato dell'automa termina.
     * Una volta invocato, viene determinato il prossimo stato attraverso {@link Switcher}, il
     * componente cardine di questa classe.
     *
     * @param state Lo stato appena conclusosi che ha invocato questo metodo.
     */


    /**
     * Permette di avviare il Server di gioco e avviare la transizione a {@link MatchStart}, il primo
     * stato dell'automa.
     */

    public void startServer() {
        CountDownLatch roomCreationSignal = new CountDownLatch(1);
        serverManager = new ServerManager(roomCreationSignal);
        new Thread(serverManager).start();
        try {
            roomCreationSignal.await();
            setState(new MatchStart(this));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public ServerManager getServerManager() {
        return serverManager;
    }

    public TurnModel getTurnModel() {
        return turnModel;
    }

    public MatchModel getMatchModel() {
        return matchModel;
    }
}
