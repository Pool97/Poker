package server.automa;

import interfaces.PokerState;
import server.controller.MatchHandler;
import server.controller.Room;
import server.events.EventsContainer;
import server.events.ServerClosedEvent;

public class RestartMatch implements PokerState {
    private MatchHandler match;

    public RestartMatch(MatchHandler match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        Room room = match.getRoom();
        room.sendBroadcastToLostPlayers(new EventsContainer(new ServerClosedEvent()));
        room.sendBroadcast(new EventsContainer(new ServerClosedEvent()));
        match.stop.countDown();
    }
}
