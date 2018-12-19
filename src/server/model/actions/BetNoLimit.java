package server.model.actions;

import interfaces.ActionManager;

public class BetNoLimit extends AbstractPokerAction  {
    private int value;

    public BetNoLimit(int value) {
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

    public static BetNoLimit empty(){
        return new BetNoLimit(0);
    }

    public boolean isEmpty(){
        return value == 0;
    }

    public String toString(){
        return "BET";
    }
}
