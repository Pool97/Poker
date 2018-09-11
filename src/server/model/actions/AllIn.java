package server.model.actions;

import interfaces.ActionManager;
import interfaces.BettingManager;
import interfaces.PokerAction;

public class AllIn implements PokerAction {
    private int value;

    public AllIn(int value) {
        this.value = value;
    }

    @Override
    public void accept(ActionManager actionManager) {

    }

    @Override
    public void process(BettingManager bettingManager) {

    }

    @Override
    public int getValue() {
        return value;
    }
}
