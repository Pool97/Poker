package events;

import interfaces.Event;

/**
 * Evento che contiene il cambiamento effettuato sullo Small Blind del match.
 */

public class BigBlindEvent implements Event {
    private int bigBlind;

    public BigBlindEvent(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }
}
