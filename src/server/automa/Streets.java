package server.automa;

import events.CommunityUpdatedEvent;
import events.Events;
import interfaces.PokerState;
import javafx.util.Pair;
import server.model.CardRank;
import server.model.CardSuit;
import server.model.TurnModel;

public class Streets implements PokerState {
    private Match match;

    public Streets(Match match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        TurnModel turnModel = match.getTurnModel();
        System.out.println("Siamo al Turn street/River");
        turnModel.getNextCard(); //la prima carta si scarta

        Pair<CardSuit, CardRank> streetCard = turnModel.getNextCard();
        turnModel.addCommunityCards(streetCard);

        match.getRoom().sendBroadcast(new Events(new CommunityUpdatedEvent(streetCard)));
        match.setState(new Action(match));
    }
}
