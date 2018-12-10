package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

/**
 * Evento generato dal Server ogni qualvolta viene aggiornato il valore del SidePot.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PotUpdated implements ServerEvent {
    private int pot;

    public PotUpdated(int pot) {
        this.pot = pot;
    }

    public int getPot() {
        return pot;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
