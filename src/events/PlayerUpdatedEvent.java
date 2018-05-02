package events;

import interfaces.Event;
import interfaces.EventProcess;
import server.model.PlayerModel;

/**
 * Evento generato dal Server ogni qualvolta il model di un Player viene modificato
 * a causa dell'evoluzione della partita.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PlayerUpdatedEvent implements Event {
    private PlayerModel player;

    public PlayerUpdatedEvent(PlayerModel player) {
        this.player = player;
    }

    public PlayerModel getPlayer() {
        return player;
    }

    @Override
    public void accept(EventProcess processor) {
        processor.process(this);
    }
}
