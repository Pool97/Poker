package server.model.actions;

import interfaces.ActionManager;
import interfaces.BettingManager;
import interfaces.PokerAction;

public class BetNoLimit implements PokerAction {
    private int value;

    public BetNoLimit(int value) {
        this.value = value;
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
    public int getValue() {
        return value;
    }

    public static BetNoLimit empty(){
        return new BetNoLimit(0);
    }

    public boolean isEmpty(){
        return value == 0;
    }
}
