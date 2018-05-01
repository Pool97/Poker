package events;

import interfaces.Event;
import interfaces.Message;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Stack;

public class Events implements Message, Serializable {
    private Stack<Event> events;

    public Events() {
        events = new Stack<>();
    }

    public Events(Event event) {
        events = new Stack<>();
    }

    public Events(Event... events) {
        this.events.addAll(Arrays.asList(events));
    }
    public void addEvent(Event event) {
        events.push(event);
    }

    public Event getEvent() {
        return events.pop();
    }

    public boolean isEmpty() {
        return events.empty();
    }

    public void removeAll() {
        events.removeAllElements();
    }

}
