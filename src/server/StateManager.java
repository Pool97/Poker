package server;

import interfaces.Observable;
import interfaces.Observer;
import server.model.MatchModel;
import server.states.StakePhase;
import server.states.StartTurn;

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
    private ServerSocketManager connectionHandler;
    private static Logger logger = Logger.getLogger(ServerSocketManager.class.getName());


    public StateManager(){
        matchModel = new MatchModel();
        MatchConfigurator start = new MatchConfigurator(matchModel);
        connectionHandler = start.getConnectionHandler();
        //far partire il primo turno
    }

    @Override
    public void update(Observable observable) {

        if(observable instanceof MatchConfigurator) {
            StartTurn startTurn = new StartTurn(matchModel);
            startTurn.attach(this);
        }

        if(observable instanceof StartTurn)
            new StakePhase();

    }
}
