package server.events;

import interfaces.EventManager;
import interfaces.ServerEvent;

public class NullEvent implements ServerEvent {

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
