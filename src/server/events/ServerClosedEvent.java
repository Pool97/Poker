package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class ServerClosedEvent implements ServerEvent {
    public ServerClosedEvent() {

    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
