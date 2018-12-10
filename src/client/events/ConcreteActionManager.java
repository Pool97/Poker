package client.events;

import client.net.Client;
import client.ui.userboard.ActionBoard;
import interfaces.ActionManager;
import server.model.actions.*;

public class ConcreteActionManager implements ActionManager {
    public ActionBoard actionBoard;

    public ConcreteActionManager(ActionBoard actionBoard) {
        this.actionBoard = actionBoard;
    }

    @Override
    public void process(Call call) {
        actionBoard.setCallEnabled(true);
        actionBoard.setCallText(" " + call.getValue() + "$");
        actionBoard.addCallListener(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(call));
            actionBoard.setCallText("");
            actionBoard.repaint();
            actionBoard.validate();

            actionBoard.setActionButtonsEnabled(false);
        });
    }

    @Override
    public void process(RaiseNoLimit raiseNoLimitOption) {
        actionBoard.setRaiseText("");
        actionBoard.setMinSlider(raiseNoLimitOption.getValue());
        actionBoard.addRaiseListener(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(new RaiseNoLimit(actionBoard.getSliderValue())));
            actionBoard.setCallText("");
            actionBoard.setActionButtonsEnabled(false);
        });
    }


    @Override
    public void process(Fold fold) {
        actionBoard.setFoldEnabled(true);
        actionBoard.addFoldListener(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(fold));
            actionBoard.setActionButtonsEnabled(false);
            actionBoard.setCallText("");
        });
    }

    @Override
    public void process(Check check) {
        actionBoard.setCheckEnabled(true);
        actionBoard.addCheckListener(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(new Check()));
            actionBoard.setActionButtonsEnabled(false);
        });
    }

    @Override
    public void process(BetNoLimit betNoLimit) {
        actionBoard.setBetText(Integer.toString(betNoLimit.getValue()));
        actionBoard.setMinSlider(betNoLimit.getValue());
        actionBoard.addRaiseListener(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(new BetNoLimit(actionBoard.getSliderValue())));
            actionBoard.setCallText("");
            actionBoard.setActionButtonsEnabled(false);
            actionBoard.setRaiseText("");
        });
    }

    @Override
    public void process(AllIn allin) {
        actionBoard.setMaxSlider(allin.getValue());
        actionBoard.refreshSlider();
        actionBoard.setRaiseEnabled(true);
    }

    @Override
    public void process(RaiseLimit raiseLimit) {
        actionBoard.setRaiseText(Integer.toString(raiseLimit.getValue()));
        actionBoard.setRaiseEnabled(true);
        actionBoard.setRaiseSliderEnabled(false);
        actionBoard.addRaiseListener(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(new RaiseLimit(raiseLimit.getValue())));
            actionBoard.setCallText("");
            actionBoard.setActionButtonsEnabled(false);
        });
    }

    @Override
    public void process(BetLimit betLimit) {
        actionBoard.setBetText(Integer.toString(betLimit.getValue()));
        actionBoard.setRaiseEnabled(true);
        actionBoard.setRaiseSliderEnabled(false);
        actionBoard.addRaiseListener(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(new BetLimit(betLimit.getValue())));
            actionBoard.setCallText("");
            actionBoard.setActionButtonsEnabled(false);
        });
    }

    @Override
    public void process(AbstractPokerAction action) {

    }
}
