package server.controller;

import java.util.concurrent.CountDownLatch;

public class ServerThread implements Runnable {
    private CountDownLatch countDownLatch;

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        MatchHandler manager = new MatchHandler(countDownLatch);
        manager.startServer();
        while (countDownLatch.getCount() > 0) {
            manager.start();
        }
    }
}
