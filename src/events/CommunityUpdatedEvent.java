package events;

import interfaces.Event;
import interfaces.EventProcess;
import javafx.util.Pair;
import server.model.CardRank;
import server.model.CardSuit;

import java.util.Stack;

public class CommunityUpdatedEvent implements Event {
    private Stack<Pair<CardSuit, CardRank>> communityCards;

    public CommunityUpdatedEvent(Pair<CardSuit, CardRank>... cards) {
        communityCards = new Stack<>();
        for (Pair<CardSuit, CardRank> card : cards)
            communityCards.push(card);
    }

    public Pair<CardSuit, CardRank> getCard() {
        return communityCards.pop();
    }

    @Override
    public void accept(EventProcess processor) {

    }
}
