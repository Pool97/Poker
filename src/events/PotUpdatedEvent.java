package events;

import interfaces.Event;

/**
 * Evento generato dal Server ogni qualvolta viene aggiornato il valore del Pot.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PotUpdatedEvent implements Event {
    private int pot;

    public PotUpdatedEvent(int pot) {
        this.pot = pot;
    }

    public int getPot() {
        return pot;
    }
}
