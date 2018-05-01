package server.automa;

import events.CommunityUpdatedEvent;
import events.Events;
import interfaces.PokerState;
import javafx.util.Pair;
import server.model.CardRank;
import server.model.CardSuit;
import server.model.TurnModel;

import java.util.concurrent.CountDownLatch;


public class Flop implements PokerState {
    private Match match;

    public Flop(Match match) {
        System.out.println("Sono al Flop!");
        this.match = match;
    }

    @Override
    public void goNext() {
        TurnModel turnModel = match.getTurnModel();
        turnModel.getNextCard(); //brucio la prima carta (non è associata a nessuna reference quindi ci penserà il GC

        /*
           Le prime tre carte della Community!
         */

        Pair<CardSuit, CardRank> firstCard = turnModel.getNextCard();
        Pair<CardSuit, CardRank> secondCard = turnModel.getNextCard();
        Pair<CardSuit, CardRank> thirdCard = turnModel.getNextCard();
        turnModel.addCommunityCards(firstCard, secondCard, thirdCard);

        match.getRoom()
                .sendBroadcast(new CountDownLatch(1), new Events(new CommunityUpdatedEvent(firstCard, secondCard, thirdCard)));
        Action action = new Action(match);
        action.setTransitionStrategy(() -> match.setState(new Streets(match)));
        match.setState(new Action(match));
    }
}
