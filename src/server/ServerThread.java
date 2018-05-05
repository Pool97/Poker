package server;

import server.automa.MatchHandler;

public class ServerThread implements Runnable {

    @Override
    public void run() {
        MatchHandler manager = new MatchHandler();
        manager.startServer();
        while (true) {
            manager.start();
        }
    }
}
