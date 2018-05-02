package events;

import interfaces.Event;
import interfaces.EventProcess;

/**
 * Evento generato dal Server ogni qualvolta vengono aggiornati i valori dei bui all'interno del Model.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class BlindsUpdatedEvent implements Event {
    private int smallBlind;
    private int bigBlind;

    public BlindsUpdatedEvent(int smallBlind, int bigBlind) {
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
    public void accept(EventProcess processor) {
        processor.process(this);
    }
}
