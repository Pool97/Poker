package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class ServerClosed implements ServerEvent {
    public ServerClosed() {

    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
