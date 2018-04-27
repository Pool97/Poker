package server.automa;

import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import interfaces.StateSwitcher;
import server.model.TurnModel;
import server.socket.ServerManager;

import java.util.ArrayList;

public class StakeState implements PokerState, Observable {
    private StakeManager stakeManager;
    static int PHASE = 0;
    private ArrayList<Observer> observers;
    private ServerManager serverHandler;

    public StakeState(TurnModel turnModel, ServerManager serverHandler) {
        observers = new ArrayList<>();
        stakeManager = new StakeManager(turnModel, serverHandler);
        this.serverHandler = serverHandler;
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
        serverHandler.logger.info("È iniziato il giro di puntate numero " + (PHASE + 1) + "\n");
        stakeManager.startStake();
        notifyObservers();
    }
}
