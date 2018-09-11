package server.automa;

import interfaces.PokerState;
import server.controller.MatchHandler;

public class Turn extends Street implements PokerState {

    public Turn(MatchHandler match) {
        super(match);
        showNextCard();
    }

    @Override
    public void goNext() {
        NextBettingRound nextAction = new NextBettingRound(match);
        nextAction.setTransitionStrategy(() -> match.setState(new River(match)));
        match.setState(nextAction);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
