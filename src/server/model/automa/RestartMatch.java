package server.model.automa;

import server.events.EventsContainer;
import server.events.ServerClosedEvent;

public class RestartMatch extends AbstractPokerState {

    public RestartMatch() {

    }

    @Override
    public void goNext(Context context) {
        table.sendBroadcastToLostPlayers(new EventsContainer(new ServerClosedEvent()));
        table.sendBroadcast(new EventsContainer(new ServerClosedEvent()));
        context.countDownLatch.countDown();
    }
}
