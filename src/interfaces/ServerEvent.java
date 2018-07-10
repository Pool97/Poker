package interfaces;

public interface ServerEvent extends Event {
    void accept(EventsManager processor);
}
