package client.messages;

import interfaces.Message;

import java.io.Serializable;

/**
 * Messaggio inviato dal creatore della stanza al Server, una volta che ha scelto tutti i parametri
 * di configurazione del Match.
 * È necessario che ogni messaggio aderisca all'interfaccia {@link Message} per fare in modo che il Server
 * sia in grado di comprenderlo, e all'interfaccia {@link Serializable} per poter essere inviato attraverso
 * un ObjectOutputStream.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class CreationRoomMessage implements Message, Serializable {
    private int maxPlayers;

    /**
     * Costruttore della classe CreationRoomMessage
     *
     * @param maxPlayers Numero massimo di giocatori scelti per il Match.
     */

    public CreationRoomMessage(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
