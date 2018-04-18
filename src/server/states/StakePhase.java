package server.states;

import server.StakeManager;
import server.interfaces.Observable;
import server.interfaces.Observer;
import server.interfaces.PokerState;

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
