package server.events;

import interfaces.EventManager;
import interfaces.ServerEvent;

public class TurnEnded implements ServerEvent {

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
