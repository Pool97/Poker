package events;

import interfaces.Event;
import interfaces.EventProcess;

public class RoomCreatedEvent implements Event {

    public RoomCreatedEvent() {
    }

    @Override
    public void accept(EventProcess processor) {
        processor.process(this);
    }
}
