package server.automa;

import interfaces.PokerState;

public class Turn extends Street implements PokerState {

    public Turn(MatchHandler match) {
        super(match);
        showNextCard();
    }

    @Override
    public void goNext() {
        NextAction nextAction = new NextAction(match);
        nextAction.setTransitionStrategy(() -> match.setState(new River(match)));
        match.setState(nextAction);
    }
}
