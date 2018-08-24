package server.model;

import interfaces.ActionManager;
import interfaces.PokerAction;

public class Bet implements PokerAction {
    private int value;

    public Bet(int value) {
        this.value = value;
    }

    @Override
    public void accept(ActionManager actionManager) {

    }

    @Override
    public int getValue() {
        return value;
    }
}
