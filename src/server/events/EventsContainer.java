package server.events;

import interfaces.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class EventsContainer implements Serializable {
    private Stack<Event> events;

    public EventsContainer() {
        events = new Stack<>();
    }

    public EventsContainer(Event event) {
        events = new Stack<>();
        events.add(event);
    }

    public EventsContainer(Event... events) {
        this.events = new Stack<>();
        this.events.addAll(Arrays.asList(events));
    }

    public EventsContainer(ArrayList<? extends Event> events) {
        this.events = new Stack<>();
        this.events.addAll(events);
    }

    public static EventsContainer createEmptyContainer() {
        return new EventsContainer();
    }

    public void addEvent(Event event) {
        events.push(event);
    }

    public Event getEvent() {
        return events.pop();
    }

    public ArrayList<Event> getEvents() {
        return new ArrayList<>(events);
    }

    public boolean isEmpty() {
        return events.empty();
    }

    public void removeAll() {
        events.removeAllElements();
    }

}
