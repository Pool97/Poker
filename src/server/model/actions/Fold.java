package server.model.actions;

import interfaces.ActionManager;

public class Fold extends AbstractPokerAction  {
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
        return "FOLD ";
    }


}
