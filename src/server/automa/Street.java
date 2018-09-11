package server.automa;

import server.controller.MatchHandler;
import server.events.CommunityUpdatedEvent;
import server.events.EventsContainer;
import server.model.TurnModel;
import server.model.cards.CardModel;

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

        match.getRoom().sendBroadcast(new EventsContainer(new CommunityUpdatedEvent(streetCard)));
    }
}
