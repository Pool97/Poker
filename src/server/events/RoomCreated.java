package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class RoomCreated implements ServerEvent {

    public RoomCreated() {
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
