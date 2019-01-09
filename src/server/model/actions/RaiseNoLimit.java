package server.model.actions;

import interfaces.ActionManager;

public class RaiseNoLimit extends AbstractPokerAction  {
    private int value;
    private int three;
    private int halfPot;
    private int pot;

    public RaiseNoLimit(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    public void setThree(int three) {
        this.three = three;
    }

    public void setHalfPot(int halfPot) {
        this.halfPot = halfPot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public int getThree() {
        return three;
    }

    public int getHalfPot() {
        return halfPot;
    }

    public int getPot() {
        return pot;
    }

    @Override
    public void accept(ActionManager actionManager) {
        actionManager.process(this);
    }

    public String toString(){
        return "RAISE";
    }
}
