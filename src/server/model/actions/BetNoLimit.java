package server.model.actions;

import interfaces.ActionManager;

public class BetNoLimit extends AbstractPokerAction  {
    private int value;
    private int pot;
    private int halfPot;

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

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public int getHalfPot() {
        return halfPot;
    }

    public void setHalfPot(int halfPot) {
        this.halfPot = halfPot;
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
