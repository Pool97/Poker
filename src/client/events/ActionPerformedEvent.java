package client.events;

import interfaces.ClientEvent;
import interfaces.PokerAction;

/**
 * È un evento generato dal PlayerBoard ha preso la sua decisione durante il suo turno.
 * Le decisioni che è in grado di effettuare sono tutte indicate in {@link server.model.ActionType}.
 * Il PlayerBoard che ha generato l'evento è determinato dal contesto.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class ActionPerformedEvent implements ClientEvent {
    private PokerAction action;

    public ActionPerformedEvent(PokerAction action) {
        this.action = action;
    }

    public PokerAction getAction() {
        return action;
    }
}
