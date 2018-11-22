package server.automa;

import interfaces.PokerState;
import server.controller.Context;
import server.controller.Room;
import server.events.EventsContainer;
import server.events.ServerClosedEvent;

public class RestartMatch implements PokerState {
    private Context match;

    public RestartMatch(Context match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        Room room = match.getRoom();
        room.sendBroadcastToLostPlayers(new EventsContainer(new ServerClosedEvent()));
        room.sendBroadcast(new EventsContainer(new ServerClosedEvent()));
        match.countDownLatch.countDown();
    }
}
