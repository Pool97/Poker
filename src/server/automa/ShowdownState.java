package server.automa;

import interfaces.PokerState;
import interfaces.StateSwitcher;

public class ShowdownState implements PokerState {
    @Override
    public void start() {

    }

    @Override
    public void accept(StateSwitcher switcher) {
        switcher.switchState(this);
    }
}
