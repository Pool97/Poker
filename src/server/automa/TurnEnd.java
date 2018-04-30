package server.automa;

import interfaces.PokerState;

import java.util.concurrent.CountDownLatch;

public class TurnEnd implements PokerState {

    @Override
    public void goNext() {
        System.out.println("TurnEnd.");

        CountDownLatch countDownLatch = new CountDownLatch(1);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
