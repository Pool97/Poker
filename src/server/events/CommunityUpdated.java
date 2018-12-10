package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;
import server.model.cards.CardModel;

import java.util.Stack;

public class CommunityUpdated implements ServerEvent {
    private Stack<CardModel> communityCards;

    public CommunityUpdated(CardModel... cards) {
        communityCards = new Stack<>();
        for (CardModel card : cards)
            communityCards.push(card);
    }

    public CardModel getCard() {
        return communityCards.pop();
    }

    public int number() {
        return communityCards.size();
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
