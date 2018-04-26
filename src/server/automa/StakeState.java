package server.automa;

import events.Events;
import events.PlayerUpdatedEvent;
import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import interfaces.StateSwitcher;
import server.model.MatchModel;
import server.model.PlayerModel;
import server.model.Position;
import server.model.Room;
import server.socket.ServerManager;

import java.util.ArrayList;

public class StakeState implements PokerState, Observable {
    private StakeManager stakeManager;
    private MatchModel matchModel;
    private ServerManager connectionHandler;
    private ArrayList<Observer> observers;

    public StakeState(MatchModel matchModel, ServerManager connectionHandler) {
        observers = new ArrayList<>();
        stakeManager = new StakeManager();
        this.matchModel = matchModel;
        this.connectionHandler = connectionHandler;
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.update(this));
    }

    @Override
    public void accept(StateSwitcher switcher) {
        switcher.switchState(this);
    }

    @Override
    public void start() {
        // Le prime due puntate del primo giro del turno
        Events stakeState = new Events();
        Room room = connectionHandler.getRoom();
        PlayerModel smallBlind = room.getPlayerByPosition(Position.SB);
        smallBlind.setTotalChips(smallBlind.getTotalChips() - matchModel.getSmallBlind());
        stakeState.addEvent(new PlayerUpdatedEvent(smallBlind));
        matchModel.increasePot(matchModel.getSmallBlind());
        PlayerModel bigBlind = room.getPlayerByPosition(Position.BB);
        bigBlind.setTotalChips(bigBlind.getTotalChips() - matchModel.getBigBlind());
        matchModel.increasePot(matchModel.getSmallBlind());
        stakeState.addEvent(new PlayerUpdatedEvent(bigBlind));
        matchModel.increaseBlinds();
    }
}
