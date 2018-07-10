package server.model;

import interfaces.ActionManager;
import interfaces.PokerAction;

public class Check implements PokerAction {

    @Override
    public void accept(ActionManager actionManager) {
        actionManager.process(this);
    }

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public String toString() {
        return "CHECK";
    }
}
