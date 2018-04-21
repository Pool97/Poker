package client.messages;

import interfaces.Message;

import java.io.Serializable;

/**
 * Messaggio inviato da tutti i Players, eccetto il creatore della stanza ({@link CreationRoomMessage}), al Server per poter fornire
 * le proprie informazioni iniziali, utili per essere visualizzate da tutti i Players sulla propria grafica.
 * È necessario che ogni messaggio aderisca all'interfaccia {@link Message} per fare in modo che il Server
 * sia in grado di comprenderlo, e all'interfaccia {@link Serializable} per poter essere inviato attraverso
 * un ObjectOutputStream.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class WelcomePlayerMessage implements Message, Serializable {
    private String nickname;
    private String avatarFilename;

    /**
     * Costruttore della classe WelcomePlayerMessage.
     *
     * @param nickname Nickname del Player
     * @param avatarFilename Il nome dell'avatar scelto.
     */

    public WelcomePlayerMessage(String nickname, String avatarFilename) {
        this.nickname = nickname;
        this.avatarFilename = avatarFilename;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarFilename() {
        return avatarFilename;
    }
}
