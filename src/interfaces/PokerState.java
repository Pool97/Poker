package interfaces;

/**
 * Interfaccia che espone quali metodi deve implementare una classe che
 * vuole rappresentare uno stato dell'automa.
 */

public interface PokerState {
    void start();

    /**
     * @param switcher
     */

    void accept(StateSwitcher switcher);

}
