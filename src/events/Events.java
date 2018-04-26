package events;

import interfaces.Event;
import interfaces.Message;

import java.io.Serializable;
import java.util.Stack;

public class Events implements Message, Serializable {
    private Stack<Event> events;

    public Events() {
        events = new Stack<>();
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

}
