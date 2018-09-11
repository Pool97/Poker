package server.model.actions;

import interfaces.ActionManager;
import interfaces.BettingManager;
import interfaces.PokerAction;

public class Call implements PokerAction {
    private int value;

    public Call(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void accept(ActionManager actionManager) {
        actionManager.process(this);
    }

    @Override
    public void process(BettingManager bettingManager) {
        bettingManager.process(this);
    }

    @Override
    public String toString() {
        return "CALL: " + value;
    }
}
