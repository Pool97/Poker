package server.model.actions;

import interfaces.ActionManager;
import interfaces.BettingManager;
import interfaces.PokerAction;

public class Raise implements PokerAction {
    private int value;

    public Raise(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void accept(ActionManager actionManager) {

    }

    @Override
    public void process(BettingManager bettingManager) {
        bettingManager.process(this);
    }
}
