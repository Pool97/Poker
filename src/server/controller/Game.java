package server.controller;

import interfaces.Event;
import interfaces.Observer;
import interfaces.PokerState;
import server.model.Table;
import server.model.automa.StartGame;
import server.model.gamestructure.BettingStructure;
import server.model.gamestructure.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class Game implements Runnable, Observer {
    private CountDownLatch countDownLatch;
    private PokerState currentState;
    private List<ConcreteReceiver> receivers;
    private BettingStructure bettingStructure;
    private GameType gameType;
    private CountDownLatch latch;

    public static final Logger logger = Logger.getLogger(Game.class.getName());

    public Game(Table room, CountDownLatch latch){
        currentState = new StartGame(room, room.getDealer());
        receivers = new ArrayList<>();
        this.latch = latch;
    }

    public void register(ConcreteReceiver receiver){
        receivers.add(receiver);
    }

    public void setNextState(PokerState state) {
        this.currentState = state;
    }

    public void setBettingStructure(BettingStructure bettingStructure){
        this.bettingStructure = bettingStructure;
    }

    public void setGameType(GameType gameType){
        this.gameType = gameType;
    }

    public int getSmallBlind(){
        return bettingStructure.getSmallBlind();
    }

    public int getBigBlind(){
        return bettingStructure.getBigBlind();
    }

    public void increaseBlinds(){
        bettingStructure.increaseBlinds();
    }

    public int getAnte(){
        return gameType.getAnte();
    }

    public BettingStructure getBettingStructure(){
        return bettingStructure;
    }


    @Override
    public void run() {
        await(latch);
        countDownLatch = new CountDownLatch(1);

        while (countDownLatch.getCount() > 0)
            currentState.goNext(this);
    }

    public void await(CountDownLatch latch){
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        countDownLatch.countDown();
    }

    public void sendMessage(Event eventsContainer){
        for(ConcreteReceiver receiver : receivers)
            new ConcreteSendCommand(receiver, eventsContainer).execute();
    }

    public Event readMessage(String nickname){
        ConcreteReceiver receiver = receivers.stream().filter(rec -> rec.getNickname().equals(nickname)).findFirst().get();
        ConcreteReadCommand readCommand = new ConcreteReadCommand(receiver);
        readCommand.execute();
        return readCommand.getMessage();
    }

    @Override
    public void update(Event event) {
        sendMessage(event);
    }
}
