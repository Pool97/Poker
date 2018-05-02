package events;

import interfaces.Event;
import interfaces.EventProcess;

/**
 * Evento generato dal Player creatore della stanza.
 * Esso contiene il numero massimo di giocatori che possono partecipare al match.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class CreatorConnectedEvent implements Event {
    private int totalPlayers;

    public CreatorConnectedEvent(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    @Override
    public void accept(EventProcess processor) {

    }
}
