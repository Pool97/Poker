package server.model;

import interfaces.ActionManager;
import interfaces.PokerAction;

public class RaiseOption implements PokerAction {
    private int minValue;
    private int maxValue;

    public RaiseOption(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public void accept(ActionManager actionManager) {
        actionManager.process(this);
    }

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public String toString() {
        return "RAISE: " + minValue + "UP TO " + maxValue;
    }
}
