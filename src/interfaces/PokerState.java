package interfaces;

import server.model.automa.Game;

/**
 * Interfaccia che espone quali metodi deve implementare una classe che
 * vuole rappresentare uno stato dell'automa.
 */

public interface PokerState {
    void goNext(Game game);
}
