package server.model;

import interfaces.ActionManager;
import interfaces.PokerAction;

public class Fittizia implements PokerAction {
    private int deltaBlind;

    public Fittizia(int deltaBlind) {
        this.deltaBlind = deltaBlind;
    }

    @Override
    public void accept(ActionManager actionManager) {

    }

    @Override
    public int getValue() {
        return deltaBlind;
    }
}
