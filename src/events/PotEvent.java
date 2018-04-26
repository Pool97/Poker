package events;

import interfaces.Event;

/**
 * Evento che contiene il cambiamento effettuato sul pot del turno.
 */

public class PotEvent implements Event {
    private int pot;

    public PotEvent(int pot) {
        this.pot = pot;
    }

    public int getPot() {
        return pot;
    }
}
