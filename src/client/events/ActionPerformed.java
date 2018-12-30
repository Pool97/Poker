package client.events;

import interfaces.ClientEvent;
import interfaces.EventManager;
import server.model.actions.AbstractPokerAction;

public class ActionPerformed implements ClientEvent {
    private AbstractPokerAction action;

    public ActionPerformed(AbstractPokerAction action) {
        this.action = action;
    }

    public AbstractPokerAction getAction() {
        return action;
    }

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
