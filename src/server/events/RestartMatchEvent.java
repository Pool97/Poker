package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class RestartMatchEvent implements ServerEvent {
    public RestartMatchEvent() {

    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
