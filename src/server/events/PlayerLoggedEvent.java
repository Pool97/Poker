package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;
import server.model.Position;

/**
 * Evento generato dal server per indicare la corretta aggiunta del PlayerBoard alla lista dei giocatori
 * della partita.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PlayerLoggedEvent implements ServerEvent {
    private String nickname;
    private String avatar;
    private Position position;
    private int chips;

    public PlayerLoggedEvent(String nickname, String avatar, Position position, int chips) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.position = position;
        this.chips = chips;
    }

    public PlayerLoggedEvent(String nickname) {
        this.nickname = nickname;
    }

    public int getChips() {
        return chips;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
