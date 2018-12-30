package client.events;

import interfaces.ClientEvent;
import interfaces.EventManager;

import java.io.Serializable;

/**
 * Evento generato dal PlayerBoard nel momento in cui la configurazione del suo personaggio
 * è conclusa.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PlayerConnected implements ClientEvent, Serializable {
    private String nickname;
    private String avatar;

    public PlayerConnected(String nickname, String avatar) {
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
