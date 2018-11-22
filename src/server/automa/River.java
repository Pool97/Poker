package server.automa;

import interfaces.PokerState;
import server.controller.Context;

public class River extends Street implements PokerState {

    public River(Context match) {
        super(match);
        showNextCard();
    }

    @Override
    public void goNext() {
        Showdown turnEnd = new Showdown(match);
        match.setState(turnEnd);
    }
}
