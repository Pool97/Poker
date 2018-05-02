package events;

import interfaces.Event;
import interfaces.EventProcess;
import server.model.Position;

/**
 * Evento generato dal server per indicare la corretta aggiunta del Player alla lista dei giocatori
 * della partita.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PlayerAddedEvent implements Event {
    private String nickname;
    private String avatar;
    private Position position;
    private int initialChips;

    public PlayerAddedEvent(String nickname, String avatar, Position position, int initialChips) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.position = position;
        this.initialChips = initialChips;
    }

    public int getInitialChips() {
        return initialChips;
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
    public void accept(EventProcess processor) {
        processor.process(this);
    }
}
