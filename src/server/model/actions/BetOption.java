package server.model.actions;

import interfaces.ActionManager;
import interfaces.BettingManager;
import interfaces.PokerAction;

public class BetOption implements PokerAction {
    private int minValue;
    private int maxValue;
    private String actionName = "BET";

    public BetOption(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void accept(ActionManager actionManager) {
        actionManager.process(this);
    }

    @Override
    public void process(BettingManager bettingManager) {

    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public String toString() {
        return "BET";
    }
}