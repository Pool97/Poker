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
    public void process(RaiseOption raiseOption) {
        actionBoard.setRaiseEnabled(true);
        actionBoard.setRaiseText("");
        actionBoard.setExtremeSliderValues(raiseOption.getMinValue(), raiseOption.getMaxValue());
        actionBoard.addRaiseListener(eventG -> {
            Client.getInstance().writeMessage(new EventsContainer(new ActionPerformedEvent(new Raise(actionBoard.getSliderValue()))));
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
    public void process(BetOption bet) {
        actionBoard.setRaiseEnabled(true);
        actionBoard.setBetText("");
        actionBoard.setExtremeSliderValues(bet.getMinValue(), bet.getMaxValue());
        actionBoard.addRaiseListener(eventG -> {
            Client.getInstance().writeMessage(new EventsContainer(new ActionPerformedEvent(new Bet(actionBoard.getSliderValue()))));
            actionBoard.setCallText("");
            actionBoard.setActionButtonsEnabled(false);
            actionBoard.setRaiseText("");
        });
    }
}
