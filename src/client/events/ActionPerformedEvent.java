package client.events;

import interfaces.ClientEvent;
import server.model.actions.AbstractPokerAction;

public class ActionPerformedEvent implements ClientEvent {
    private AbstractPokerAction action;

    public ActionPerformedEvent(AbstractPokerAction action) {
        this.action = action;
    }

    public AbstractPokerAction getAction() {
        return action;
    }
}
