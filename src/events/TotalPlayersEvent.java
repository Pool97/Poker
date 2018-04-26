package events;

import interfaces.Event;


public class TotalPlayersEvent implements Event {
    private int totalPlayers;

    public TotalPlayersEvent(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }
}
