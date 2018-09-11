package server.model.actions;

import interfaces.ActionManager;
import interfaces.BettingManager;
import interfaces.PokerAction;

public class DeadMoney implements PokerAction {
    private int deltaBlind;

    public DeadMoney(int deltaBlind) {
        this.deltaBlind = deltaBlind;
    }

    @Override
    public void accept(ActionManager actionManager) {

    }

    @Override
    public void process(BettingManager bettingManager) {

    }

    @Override
    public int getValue() {
        return deltaBlind;
    }
}
