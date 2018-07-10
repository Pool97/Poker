package client.events;

import interfaces.ClientEvent;

/**
 * Evento generato dal PlayerBoard creatore della stanza.
 * Esso contiene il numero massimo di giocatori che possono partecipare al match.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class CreatorConnectedEvent implements ClientEvent {
    private int totalPlayers;

    public CreatorConnectedEvent(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }
}
