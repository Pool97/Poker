package client.events;

import client.net.Client;
import client.ui.userboard.ActionBoard;
import interfaces.ActionManager;
import server.events.EventsContainer;
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
            Client.getInstance().writeMessage(new EventsContainer(new ActionPerformedEvent(call)));
            actionBoard.setCallText("");
            actionBoard.repaint();
            actionBoard.validate();

            actionBoard.setActionButtonsEnabled(false);
        });
    }

    @Override
    public void process(RaiseNoLimit raiseNoLimitOption) {
        System.out.println("PROVA RAISE LATO CLIENT");
        actionBoard.setRaiseText("RAISE");
        actionBoard.setMinSlider(raiseNoLimitOption.getValue());
        actionBoard.addRaiseListener(eventG -> {
            Client.getInstance().writeMessage(new EventsContainer(new ActionPerformedEvent(new RaiseNoLimit(actionBoard.getSliderValue()))));
            actionBoard.setCallText("");
            actionBoard.setActionButtonsEnabled(false);
        });
    }


    @Override
    public void process(Fold fold) {
        actionBoard.setFoldEnabled(true);
        actionBoard.addFoldListener(eventG -> {
            Client.getInstance().writeMessage(new EventsContainer(new ActionPerformedEvent(fold)));
            actionBoard.setActionButtonsEnabled(false);
            actionBoard.setCallText("");
        });
    }

    @Override
    public void process(Check check) {
        actionBoard.setCheckEnabled(true);
        actionBoard.addCheckListener(eventG -> {
            Client.getInstance().writeMessage(new EventsContainer(new ActionPerformedEvent(new Check())));
            actionBoard.setActionButtonsEnabled(false);
        });
    }

    @Override
    public void process(BetNoLimit betNoLimit) {
        actionBoard.setBetText("BET");
        actionBoard.setMinSlider(betNoLimit.getValue());
        actionBoard.addRaiseListener(eventG -> {
            Client.getInstance().writeMessage(new EventsContainer(new ActionPerformedEvent(new BetNoLimit(actionBoard.getSliderValue()))));
            actionBoard.setCallText("");
            actionBoard.setActionButtonsEnabled(false);
            actionBoard.setRaiseText("");
        });
    }

    @Override
    public void process(AllIn allin) {
        System.out.println("PROVA ALL IN LATO CLIENT");
        actionBoard.setMaxSlider(allin.getValue());
        actionBoard.refreshSlider();
        actionBoard.setRaiseEnabled(true);
    }
}
