package events;

import interfaces.Event;
import server.model.StakeAction;

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
    private StakeAction action;

    public ActionPerformedEvent(StakeAction action) {
        this.action = action;
    }

    public StakeAction getAction() {
        return action;
    }
}
