package server.events;

import interfaces.EventManager;
import interfaces.ServerEvent;

public class RoomCreated implements ServerEvent {

    public RoomCreated() {
    }

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
