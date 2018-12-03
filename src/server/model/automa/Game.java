package server.model.automa;

import interfaces.PokerState;
import server.controller.ConcreteReadCommand;
import server.controller.ConcreteReceiver;
import server.controller.ConcreteSendCommand;
import server.events.EventsContainer;
import server.model.Table;
import server.model.gamestructure.BettingStructure;
import server.model.gamestructure.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class Game implements Runnable{
    private CountDownLatch countDownLatch;
    private PokerState currentState;
    private List<ConcreteReceiver> receivers;
    private BettingStructure bettingStructure;
    private GameType gameType;

    public static final Logger logger = Logger.getLogger(Game.class.getName());

    public Game(Table room){
        currentState = new StartGame(room, room.getDealer());
        receivers = new ArrayList<>();
    }

    public void register(ConcreteReceiver receiver){
        receivers.add(receiver);
    }

    public void setState(PokerState state) {
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
    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);

        while (countDownLatch.getCount() > 0)
            currentState.goNext(this);
    }

    public void stop(){
        countDownLatch.countDown();
    }

    public void sendMessage(EventsContainer eventsContainer){
        for(ConcreteReceiver receiver : receivers)
            new ConcreteSendCommand(receiver, eventsContainer).execute();
    }

    public EventsContainer readMessage(String nickname){
        ConcreteReceiver receiver = receivers.stream().filter(rec -> rec.getNickname().equals(nickname)).findFirst().get();
        ConcreteReadCommand readCommand = new ConcreteReadCommand(receiver);
        readCommand.execute();
        return readCommand.getMessage();
    }
}
