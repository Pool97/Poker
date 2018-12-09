package server.model.actions;

import interfaces.ActionManager;

public class Call extends AbstractPokerAction  {
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
    public String toString() {
        return "CALL: " + value;
    }
}
