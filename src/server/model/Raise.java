package server.model;

import interfaces.ActionManager;
import interfaces.PokerAction;

public class Raise implements PokerAction {
    private int value;

    public Raise(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void accept(ActionManager actionManager) {

    }
}
