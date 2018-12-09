package server.model.actions;

import interfaces.ActionManager;

public class BetLimit extends AbstractPokerAction {
    private int value;

    public BetLimit(int value) {
        this.value = value;
    }

    @Override
    public void accept(ActionManager actionManager) {
        actionManager.process(this);
    }

    @Override
    public int getValue() {
        return value;
    }

    public static BetLimit empty(){
        return new BetLimit(0);
    }

    public boolean isEmpty(){
        return value == 0;
    }
}
