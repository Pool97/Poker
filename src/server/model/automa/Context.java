package server.model.automa;

import interfaces.PokerState;
import server.model.Table;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class Context implements Runnable {
    public CountDownLatch countDownLatch;
    private PokerState currentState;

    public static final Logger logger = Logger.getLogger(Context.class.getName());

    public Context(Table room){
        currentState = new StartGame(room, room.getDealer());
    }

    public void setState(PokerState state) {
        this.currentState = state;
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        while (countDownLatch.getCount() > 0) {
            currentState.goNext(this);
        }
    }
}
