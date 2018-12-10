package interfaces;

public interface Receiver {
    void sendMessage(Event eventsContainer);
    Event readMessage();
}
