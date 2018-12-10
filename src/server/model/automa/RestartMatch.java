package server.model.automa;

import server.controller.Game;
import server.events.ServerClosed;

public class RestartMatch extends AbstractPokerState {

    public RestartMatch() {

    }

    @Override
    public void goNext(Game game) {
        game.sendMessage(new ServerClosed());
        game.stop();
    }
}
