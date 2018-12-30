package client.events;

import interfaces.ClientEvent;
import interfaces.EventManager;

public class MatchCanStart implements ClientEvent {

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
