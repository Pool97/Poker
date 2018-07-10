package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class RoomCreatedEvent implements ServerEvent {

    public RoomCreatedEvent() {
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
