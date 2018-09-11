package server.model.actions;

import interfaces.ActionManager;
import interfaces.BettingManager;
import interfaces.PokerAction;

public class Check implements PokerAction {

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
        return 0;
    }

    @Override
    public String toString() {
        return "CHECK";
    }
}
