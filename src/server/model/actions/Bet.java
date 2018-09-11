package server.model.actions;

import interfaces.ActionManager;
import interfaces.BettingManager;
import interfaces.PokerAction;

public class Bet implements PokerAction {
    private int value;

    public Bet(int value) {
        this.value = value;
    }

    @Override
    public void accept(ActionManager actionManager) {

    }

    @Override
    public void process(BettingManager bettingManager) {
        bettingManager.process(this);
    }

    @Override
    public int getValue() {
        return value;
    }
}
