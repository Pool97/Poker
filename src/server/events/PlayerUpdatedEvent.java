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

    public PlayerUpdatedEvent(String nickname, int chips) {
        this.nickname = nickname;
        this.chips = chips;
    }

    public String getNickname() {
        return nickname;
    }

    public int getChips() {
        return chips;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
