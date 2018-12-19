package server.model.actions;

import interfaces.ActionManager;

public class RaiseLimit extends AbstractPokerAction  {
    private int value;

    public RaiseLimit(int value){
        this.value = value;
    }

    @Override
    public void accept(ActionManager actionManager) {
        actionManager.process(this);
    }

    @Override
    public int getValue() {
        return value;
    }

    public String toString(){
        return "RAISE";
    }
}
