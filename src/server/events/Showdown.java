package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class Showdown implements ServerEvent {

    public Showdown() {

    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
