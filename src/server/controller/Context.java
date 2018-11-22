package server.controller;

import interfaces.PokerState;
import server.automa.StartGame;
import server.model.MatchModel;
import server.model.TurnModel;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class Context implements Runnable {
    private MatchModel matchModel;
    private TurnModel turnModel;
    public CountDownLatch countDownLatch;
    private PokerState currentState;
    private Room room;

    public static final Logger logger = Logger.getLogger(Context.class.getName());

    public Context(Room room){
        matchModel = new MatchModel();
        turnModel = new TurnModel();
        this.room = room;
    }

    public void setState(PokerState state) {
        this.currentState = state;
    }

    public void startGame() {
        setState(new StartGame(this));
    }
    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        startGame();
        while (countDownLatch.getCount() > 0) {
            currentState.goNext();
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
