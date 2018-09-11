package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

/**
 * Evento generato dal Server ogni qualvolta il model di un PlayerBoard viene modificato
 * a causa dell'evoluzione della partita.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PlayerUpdatedEvent implements ServerEvent {
    private String nickname;
    private int chips;
    private String action;

    public PlayerUpdatedEvent(String nickname, int chips, String action) {
        this.nickname = nickname;
        this.chips = chips;
        this.action = action;
    }

    public String getNickname() {
        return nickname;
    }

    public int getChips() {
        return chips;
    }

    public String getAction() {
        return action;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
