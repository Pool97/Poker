package server.model.automa;

import interfaces.PokerState;
import server.events.CommunityUpdatedEvent;
import server.events.EventsContainer;

public class River extends Street implements PokerState {

    @Override
    public void goNext(Game game) {
        dealer.burnCard();
        game.sendMessage(new EventsContainer(new CommunityUpdatedEvent(dealer.dealCommunityCard())));
        game.setState(new Showdown());
    }
}
