package server.model.actions;

import interfaces.ActionManager;
import interfaces.BettingManager;
import interfaces.PokerAction;

public class NullAction implements PokerAction {
    private static NullAction instance;

    private NullAction(){

    }

    public static NullAction getInstance(){
        if(instance == null)
            instance = new NullAction();
        return instance;
    }

    @Override
    public void accept(ActionManager actionManager) {

    }

    @Override
    public void process(BettingManager bettingManager) {

    }

    @Override
    public int getValue() {
        return 0;
    }
}
