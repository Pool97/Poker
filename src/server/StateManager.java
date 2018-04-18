package server;

import server.interfaces.Observable;
import server.interfaces.Observer;
import server.model.MatchModel;
import server.socket.ServerSocketManager;
import server.states.StakePhase;
import server.states.StartMatch;
import server.states.StartTurn;

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

    public StateManager(){
        ServerSocketManager connectionHandler = new ServerSocketManager();
        matchModel = new MatchModel();
        StartMatch startMatch = new StartMatch(matchModel);
        startMatch.attach(this);
        startMatch.notifyAllPlayers();
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

    public static void main(String [] args){
        StateManager manager = new StateManager();
    }
}
