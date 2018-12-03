package server.model.actions;

import interfaces.ActionManager;
import interfaces.BettingManager;
import interfaces.PokerAction;

public class AllIn implements PokerAction {
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
    public void process(BettingManager bettingManager) {

    }

    @Override
    public int getValue() {
        return value;
    }
}
