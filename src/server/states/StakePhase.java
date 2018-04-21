package server.states;

import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import server.StakeManager;

import java.util.ArrayList;

public class StakePhase implements PokerState, Observable {
    private StakeManager stakeManager;
    private ArrayList<Observer> observers;

    public StakePhase(){
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
}
