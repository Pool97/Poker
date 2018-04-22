package server.automa;

import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import interfaces.StateSwitcher;

import java.util.ArrayList;

public class StakeState implements PokerState, Observable {
    private StakeManager stakeManager;
    private ArrayList<Observer> observers;

    public StakeState() {
        observers = new ArrayList<>();
        stakeManager = new StakeManager();
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
        observers.stream().forEach(observer -> observer.update(this));
    }

    @Override
    public void accept(StateSwitcher switcher) {
        switcher.switchState(this);
    }

    @Override
    public void start() {

    }
}
