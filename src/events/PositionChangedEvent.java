package events;

import interfaces.Event;
import server.model.Position;

import java.io.Serializable;

/**
 * Evento generato dal Server per informare il passaggio del turno al Player che ha
 * per posizione, la posizione contenuta nell'oggetto rappresentante l'evento.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PositionChangedEvent implements Event, Serializable {
    private Position actualPosition;

    public PositionChangedEvent(Position actualPosition) {
        this.actualPosition = actualPosition;
    }

    public Position getActualPosition() {
        return actualPosition;
    }
}
