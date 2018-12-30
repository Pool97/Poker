package interfaces;

public interface ClientEvent extends Event {
    void accept(EventManager processor);
}
