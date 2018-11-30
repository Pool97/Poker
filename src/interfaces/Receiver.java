package interfaces;

import server.events.EventsContainer;

public interface Receiver {
    void sendMessage(EventsContainer eventsContainer);
    EventsContainer readMessage();
}
