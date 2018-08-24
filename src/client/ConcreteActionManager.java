package client;

import client.components.ActionBoard;
import client.events.ActionPerformedEvent;
import client.socket.ClientManager;
import client.socket.SocketWriter;
import interfaces.ActionManager;
import server.events.Events;
import server.model.*;

public class ConcreteActionManager implements ActionManager {
    public ActionBoard actionBoard;
    private ClientManager clientManager;

    public ConcreteActionManager(ClientManager clientManager, ActionBoard actionBoard) {
        this.actionBoard = actionBoard;
        this.clientManager = clientManager;
    }

    @Override
    public void process(Call call) {
        actionBoard.setCallEnabled(true);
        actionBoard.setCallText(" " + call.getValue() + "$");
        actionBoard.addCallListener(eventG -> {
            SocketWriter called = new SocketWriter(clientManager.getOutputStream(),
                    new Events(new ActionPerformedEvent(call)));
            called.execute();
            actionBoard.setCallText("");
            actionBoard.repaint();
            actionBoard.validate();

            actionBoard.setActionButtonsEnabled(false);
        });
    }

    @Override
    public void process(RaiseOption raiseOption) {
        actionBoard.setRaiseEnabled(true);

        actionBoard.setExtremeSliderValues(raiseOption.getMinValue(), raiseOption.getMaxValue());
        actionBoard.addRaiseListener(eventG -> {
            SocketWriter called = new SocketWriter(clientManager.getOutputStream(),
                    new Events(new ActionPerformedEvent(new Raise(actionBoard.getSliderValue()))));
            called.execute();
            actionBoard.setCallText("");
            actionBoard.setActionButtonsEnabled(false);
        });
    }


    @Override
    public void process(Fold fold) {
        actionBoard.setFoldEnabled(true);
        actionBoard.addFoldListener(eventG -> {
            SocketWriter called = new SocketWriter(clientManager.getOutputStream(),
                    new server.events.Events(new ActionPerformedEvent(fold)));
            called.execute();
            actionBoard.setActionButtonsEnabled(false);
            actionBoard.setCallText("");
        });
    }

    @Override
    public void process(Check check) {
        actionBoard.setCheckEnabled(true);
        actionBoard.addCheckListener(eventG -> {
            SocketWriter called = new SocketWriter(clientManager.getOutputStream(),
                    new Events(new ActionPerformedEvent(new Check())));
            called.execute();
            actionBoard.setActionButtonsEnabled(false);
        });
    }

    @Override
    public void process(BetOption bet) {
        actionBoard.setRaiseEnabled(true);
        actionBoard.setBetText("");
        actionBoard.setExtremeSliderValues(bet.getMinValue(), bet.getMaxValue());
        actionBoard.addRaiseListener(eventG -> {
            SocketWriter called = new SocketWriter(clientManager.getOutputStream(),
                    new Events(new ActionPerformedEvent(new Bet(actionBoard.getSliderValue()))));
            called.execute();
            actionBoard.setCallText("");
            actionBoard.setActionButtonsEnabled(false);
            actionBoard.setRaiseText("");
        });
    }
}
