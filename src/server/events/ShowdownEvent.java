package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

import java.util.HashMap;

public class ShowdownEvent implements ServerEvent {
    private HashMap<String, String> points;

    public ShowdownEvent(HashMap<String, String> points) {
        this.points = points;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }

    public HashMap<String, String> getPoints() {
        return points;
    }
}
