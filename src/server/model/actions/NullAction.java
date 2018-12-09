package server.model.actions;

import interfaces.ActionManager;

public class NullAction extends AbstractPokerAction {
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
    public int getValue() {
        return 0;
    }
}
