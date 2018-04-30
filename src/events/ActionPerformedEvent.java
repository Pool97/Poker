package events;

import interfaces.Event;
import javafx.util.Pair;
import server.model.ActionType;

/**
 * È un evento generato dal Player ha preso la sua decisione durante il suo turno.
 * Le decisioni che è in grado di effettuare sono tutte indicate in {@link server.model.ActionType}.
 * Il Player che ha generato l'evento è determinato dal contesto.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class ActionPerformedEvent implements Event {
    private Pair<ActionType, Integer> action;

    public ActionPerformedEvent(Pair<ActionType, Integer> action) {
        this.action = action;
    }

    public Pair<ActionType, Integer> getAction() {
        return action;
    }
}
