package server.model.actions;

import interfaces.ActionManager;

public class AllIn extends AbstractPokerAction {
    private int value;
    private boolean optional;

    public AllIn(int value, boolean optional) {
        this.value = value;
        this.optional = optional;
    }

    @Override
    public void accept(ActionManager actionManager) {
        actionManager.process(this);
    }

    @Override
    public int getValue() {
        return value;
    }

    public String toString(){
        return "ALL IN";
    }
}
