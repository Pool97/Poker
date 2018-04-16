package server;

import java.util.ArrayList;

public class StakeTurn implements PokerState, Observable {
    private StakeManager stakeManager;
    private ArrayList<Observer> observers;

    public StakeTurn(){
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
    public void update() {
        for(Observer observer : observers){
            observer.update(this);
        }
    }
}
