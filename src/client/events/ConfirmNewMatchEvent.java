package client.events;

import interfaces.ClientEvent;

public class ConfirmNewMatchEvent implements ClientEvent {
    private boolean newMatch;

    public ConfirmNewMatchEvent(boolean newMatch) {
        this.newMatch = newMatch;
    }

    public boolean isNewMatch() {
        return newMatch;
    }

}
