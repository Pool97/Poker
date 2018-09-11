package client.events;

import interfaces.ClientEvent;

public class CreatorRestartEvent implements ClientEvent {
    public boolean restart;

    public CreatorRestartEvent(boolean restart) {
        this.restart = restart;
    }

    public boolean hasRestart() {
        return restart;
    }
}
