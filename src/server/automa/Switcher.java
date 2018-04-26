package server.automa;

import interfaces.StateSwitcher;
import server.model.MatchModel;
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
    private MatchModel matchModel;
    private ServerManager connectionHandler;
    private StateManager manager;

    /**
     * Costruttore vuoto della classe Switcher.
     *
     * @param matchModel        Model del match.
     * @param connectionHandler Gestore della connessione.
     */

    public Switcher(StateManager stateManager, MatchModel matchModel, ServerManager connectionHandler) {
        this.matchModel = matchModel;
        this.connectionHandler = connectionHandler;
        this.manager = stateManager;
    }

    @Override
    public void switchState(StartMatch state) {
        StartTurn startTurn = new StartTurn(matchModel, connectionHandler);
        startTurn.start();
    }

    @Override
    public void switchState(StartTurn state) {
        StakeState stakeState = new StakeState(matchModel, connectionHandler);
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
