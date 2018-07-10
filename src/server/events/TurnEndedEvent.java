package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class TurnEndedEvent implements ServerEvent {

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
