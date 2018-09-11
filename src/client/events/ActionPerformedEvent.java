package client.events;

import interfaces.ClientEvent;
import interfaces.PokerAction;

public class ActionPerformedEvent implements ClientEvent {
    private PokerAction action;

    public ActionPerformedEvent(PokerAction action) {
        this.action = action;
    }

    public PokerAction getAction() {
        return action;
    }
}
