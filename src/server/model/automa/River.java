package server.model.automa;

import interfaces.PokerState;
import server.events.CommunityUpdatedEvent;
import server.events.EventsContainer;

public class River extends Street implements PokerState {

    public River() {
        super();
        showNextCard();
        table.sendBroadcast(new EventsContainer(new CommunityUpdatedEvent(table.getCommunityModel().getCard(4))));
    }

    @Override
    public void goNext(Context context) {
        Showdown turnEnd = new Showdown();
        context.setState(turnEnd);
    }
}
