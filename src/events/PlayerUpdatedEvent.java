package events;

import interfaces.Event;
import server.model.PlayerModel;

public class PlayerUpdatedEvent implements Event {
    private PlayerModel player;

    public PlayerUpdatedEvent(PlayerModel player) {
        this.player = player;
    }

    public PlayerModel getPlayer() {
        return player;
    }
}
