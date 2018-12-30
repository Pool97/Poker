package server.events;

import interfaces.EventManager;
import interfaces.ServerEvent;

public class ServerClosed implements ServerEvent {

    public ServerClosed() {

    }

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
