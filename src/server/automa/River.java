package server.automa;

import interfaces.PokerState;

public class River extends Street implements PokerState {

    public River(MatchHandler match) {
        super(match);
        showNextCard();
    }

    @Override
    public void goNext() {
        Showdown turnEnd = new Showdown(match);
        match.setState(turnEnd);
    }
}
