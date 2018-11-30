package server.model.automa;

import server.events.EventsContainer;
import server.events.ServerClosedEvent;

public class RestartMatch extends AbstractPokerState {

    public RestartMatch() {

    }

    @Override
    public void goNext(Game game) {
        game.sendMessage(new EventsContainer(new ServerClosedEvent()));
        game.stop();
    }
}
