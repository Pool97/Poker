package client.events;

import client.net.Client;
import client.ui.userboard.ActionBoard;
import interfaces.ActionManager;
import server.model.actions.*;

import static client.ui.userboard.ActionBoard.ActionIndexList.*;

public class ConcreteActionManager implements ActionManager {
    public ActionBoard actionBoard;

    public ConcreteActionManager(ActionBoard actionBoard) {
        this.actionBoard = actionBoard;
    }

    @Override
    public void process(Call call) {
        actionBoard.setButtonEnabled(true, CALL);
        actionBoard.setButtonText(call.getValue(), CALL);
        actionBoard.addListenerTo(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(call));
            actionBoard.setButtonText(0, CALL);
            actionBoard.repaint();
            actionBoard.validate();
            actionBoard.setActionButtonsEnabled(false);
            }, CALL);
    }

    @Override
    public void process(RaiseNoLimit raiseNoLimitOption) {
        actionBoard.setButtonEnabled(true, RAISE);
        actionBoard.setMinSlider(raiseNoLimitOption.getValue());
        actionBoard.addListenerTo(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(new RaiseNoLimit(actionBoard.getSliderValue())));
            actionBoard.setButtonText(0, CALL);
            actionBoard.setActionButtonsEnabled(false);
        }, RAISE);
    }


    @Override
    public void process(Fold fold) {
        actionBoard.setButtonEnabled(true, FOLD);
        actionBoard.addListenerTo(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(fold));
            actionBoard.setActionButtonsEnabled(false);
            actionBoard.setButtonText(0, CALL);
        }, FOLD);
    }

    @Override
    public void process(Check check) {
        actionBoard.setButtonEnabled(true, CHECK);
        actionBoard.addListenerTo(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(new Check()));
            actionBoard.setActionButtonsEnabled(false);
        }, CHECK);
    }

    @Override
    public void process(BetNoLimit betNoLimit) {
        actionBoard.setMinSlider(betNoLimit.getValue());
        actionBoard.setButtonEnabled(true, BET);
        actionBoard.addListenerTo(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(new BetNoLimit(actionBoard.getSliderValue())));
            actionBoard.setButtonText(0, CALL);
            actionBoard.setActionButtonsEnabled(false);
        }, BET);
    }

    @Override
    public void process(AllIn allin) {
        actionBoard.setMaxSlider(allin.getValue());
        actionBoard.refreshSlider();
        actionBoard.setButtonEnabled(true, ALL_IN);
        actionBoard.addListenerTo(event -> {
            Client.getInstance().writeMessage(new ActionPerformed(new AllIn(allin.getValue(), false)));
            actionBoard.setButtonText(0, CALL);
            actionBoard.setActionButtonsEnabled(false);
        }, ALL_IN);
    }

    @Override
    public void process(RaiseLimit raiseLimit) {
        actionBoard.setButtonText(raiseLimit.getValue(), RAISE);
        actionBoard.setButtonEnabled(true, RAISE);
        actionBoard.setRaiseSliderEnabled(false);
        actionBoard.addListenerTo(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(new RaiseLimit(raiseLimit.getValue())));
            actionBoard.setButtonText(0, CALL);
            actionBoard.setButtonText(0, RAISE);
            actionBoard.setActionButtonsEnabled(false);
        }, RAISE);
    }

    @Override
    public void process(BetLimit betLimit) {
        actionBoard.setButtonText(betLimit.getValue(), BET);
        actionBoard.setButtonEnabled(true, BET);
        actionBoard.setRaiseSliderEnabled(false);
        actionBoard.addListenerTo(eventG -> {
            Client.getInstance().writeMessage(new ActionPerformed(new BetLimit(betLimit.getValue())));
            actionBoard.setButtonText(0, CALL);
            actionBoard.setActionButtonsEnabled(false);
        }, BET);
    }

    @Override
    public void process(AbstractPokerAction action) {

    }
}
