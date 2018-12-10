package server.model.automa;

import interfaces.PokerState;
import server.controller.Game;
import server.events.CommunityUpdated;

public class River extends Street implements PokerState {

    @Override
    public void goNext(Game game) {
        dealer.burnCard();
        game.sendMessage(new CommunityUpdated(dealer.dealCommunityCard()));
        game.setState(new Showdown());
    }
}
