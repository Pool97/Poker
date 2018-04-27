package server.automa;

import interfaces.StateSwitcher;
import server.model.TurnModel;
import server.socket.ServerManager;

/**
 * Lo Switcher è l'elemento cardine di {@link StateManager}.
 * Esso, attraverso il Visitor pattern, permette di determinare, data la conclusione di uno stato, il prossimo stato su
 * cui effetturare la transizione dell'automa.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Switcher implements StateSwitcher {
    private TurnModel turnModel;
    private ServerManager connectionHandler;
    private StateManager manager;

    /**
     * Costruttore vuoto della classe Switcher.
     *
     * @param turnModel        Model del match.
     * @param connectionHandler Gestore della connessione.
     */

    public Switcher(StateManager stateManager, TurnModel turnModel, ServerManager connectionHandler) {
        this.turnModel = turnModel;
        this.connectionHandler = connectionHandler;
        this.manager = stateManager;
    }

    @Override
    public void switchState(StartMatch state) {
        StartTurn startTurn = new StartTurn(turnModel, connectionHandler);
        startTurn.attach(manager);
        startTurn.start();
    }

    @Override
    public void switchState(StartTurn state) {
        StakeState stakeState = new StakeState(turnModel, connectionHandler);
        stakeState.attach(manager);
        stakeState.start();
    }

    @Override
    public void switchState(StakeState state) {
        //TODO: da implementare
    }

    @Override
    public void switchState(ShowdownState state) {
        //TODO: da implementare
    }

}
