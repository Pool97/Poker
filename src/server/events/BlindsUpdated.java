package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

/**
 * Evento generato dal Server ogni qualvolta vengono aggiornati i valori dei bui all'interno del Model.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class BlindsUpdated implements ServerEvent {
    private int smallBlind;
    private int bigBlind;

    public BlindsUpdated(int smallBlind, int bigBlind) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
