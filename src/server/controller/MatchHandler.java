package server.controller;

import interfaces.PokerState;
import server.automa.StartGame;
import server.model.MatchModel;
import server.model.TurnModel;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class MatchHandler {
    private MatchModel matchModel;
    private TurnModel turnModel;
    private Room room;
    private PokerState currentState;
    public CountDownLatch stop;

    public static final Logger logger = Logger.getLogger(MatchHandler.class.getName());

    public MatchHandler(CountDownLatch stopServer) {
        matchModel = new MatchModel();
        turnModel = new TurnModel();
        room = new Room();
        this.stop = stopServer;
    }

    public void setState(PokerState state) {
        this.currentState = state;
    }

    public void start() {
        currentState.goNext();
    }

    public void startServer() {
        CountDownLatch roomCreationSignal = new CountDownLatch(1);
        ServerManager serverManager = new ServerManager(room, roomCreationSignal);
        new Thread(serverManager).start();
        try {
            roomCreationSignal.await();
            setState(new StartGame(this));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setInitialParameters(int startChips) {
        matchModel.setStartChips(startChips);
        matchModel.setInitialBlinds();
    }

    public TurnModel getTurnModel() {
        return turnModel;
    }

    public MatchModel getMatchModel() {
        return matchModel;
    }

    public Room getRoom() {
        return room;
    }
}
