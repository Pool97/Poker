package events;

import interfaces.Event;

/**
 * Evento che contiene il cambiamento effettuato sullo Small Blind del match.
 */

public class SmallBlindEvent implements Event {
    private int smallBlind;

    public SmallBlindEvent(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public int getSmallBlind() {
        return smallBlind;
    }
}
