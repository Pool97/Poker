package interfaces;

import server.automa.ShowdownState;
import server.automa.StakeState;
import server.automa.StartMatch;
import server.automa.StartTurn;

/**
 * Interfaccia che espone i metodi che uno Switcher deve implementare
 * per poter effettuare le transizioni dei vari stati dell'automa.
 */

public interface StateSwitcher {
    void switchState(StartMatch state);

    void switchState(StartTurn state);

    void switchState(StakeState state);

    void switchState(ShowdownState state);
}
