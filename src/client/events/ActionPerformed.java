package client.events;

import interfaces.ClientEvent;
import server.model.actions.AbstractPokerAction;

public class ActionPerformed implements ClientEvent {
    private AbstractPokerAction action;

    public ActionPerformed(AbstractPokerAction action) {
        this.action = action;
    }

    public AbstractPokerAction getAction() {
        return action;
    }
}
