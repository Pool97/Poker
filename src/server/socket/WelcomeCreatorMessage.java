package server.socket;

import server.interfaces.Message;

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

public class WelcomeCreatorMessage implements Message, Serializable {
    private String nickname;
    private int maxPlayers;
    private int totalChips;
    private String avatarFilename;

    /**
     * Costruttore della classe WelcomeCreatorMessage
     *
     * @param nickname Nickname del creatore della stanza.
     * @param maxPlayers Numero massimo di giocatori scelti per il Match.
     * @param totalChips Numero totale di chips iniziali per giocatore.
     * @param avatarFilename Il nome dell'avatar scelto.
     */

    public WelcomeCreatorMessage(String nickname, int maxPlayers, int totalChips, String avatarFilename) {
        this.nickname = nickname;
        this.maxPlayers = maxPlayers;
        this.totalChips = totalChips;
        this.avatarFilename = avatarFilename;
    }

    public String getNickname() {
        return nickname;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getTotalChips() {
        return totalChips;
    }

    public String getAvatarFilename() {
        return avatarFilename;
    }
}
