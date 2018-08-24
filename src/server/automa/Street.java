package server.automa;

import server.events.CommunityUpdatedEvent;
import server.events.Events;
import server.model.CardModel;
import server.model.TurnModel;

public abstract class Street {
    protected MatchHandler match;

    public Street(MatchHandler match) {
        this.match = match;
    }

    protected void showNextCard() {
        TurnModel turnModel = match.getTurnModel();
        turnModel.getNextCard();

        CardModel streetCard = turnModel.getNextCard();
        turnModel.addCommunityCards(streetCard);

        match.getRoom().sendBroadcast(new Events(new CommunityUpdatedEvent(streetCard)));
    }
}
