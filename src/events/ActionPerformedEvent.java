package events;

import interfaces.Event;
import interfaces.EventProcess;
import javafx.util.Pair;
import server.model.ActionType;

/**
 * È un evento generato dal PlayerBoard ha preso la sua decisione durante il suo turno.
 * Le decisioni che è in grado di effettuare sono tutte indicate in {@link server.model.ActionType}.
 * Il PlayerBoard che ha generato l'evento è determinato dal contesto.
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

    @Override
    public void accept(EventProcess processor) {
    }
}
