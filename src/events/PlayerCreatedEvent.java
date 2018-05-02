package events;

import interfaces.Event;
import interfaces.EventProcess;

import java.io.Serializable;

/**
 * Evento generato dal Player nel momento in cui la configurazione del suo personaggio
 * è conclusa.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PlayerCreatedEvent implements Event, Serializable {
    private String nickname;
    private String avatar;

    public PlayerCreatedEvent(String nickname, String avatar) {
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
    public void accept(EventProcess processor) {
    }
}
