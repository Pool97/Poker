package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class ShowdownEvent implements ServerEvent {

    public ShowdownEvent() {

    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
