package server.model.actions;

import interfaces.ActionManager;

public class RaiseNoLimit extends AbstractPokerAction  {
    private int value;

    public RaiseNoLimit(int value) {
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

    public String toString(){
        return "RAISE";
    }
}
