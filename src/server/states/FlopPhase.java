package server.states;

import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import server.model.DeckModel;

import java.util.ArrayList;


public class FlopPhase implements PokerState, Observable {
    private ArrayList<Observer> observers;

    public FlopPhase(DeckModel deckModel){
        observers = new ArrayList<>();

        //ottieni le prime tre carte per la Community Cards (in realtà è da bruciare una carta per ogni carta della community)
        deckModel.nextCard();
        deckModel.nextCard();
        deckModel.nextCard();
        //notifyClients()
        //update Observers aggiornare i clients della modifica della Community Cards
    }


    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers)
            observer.update(this);
    }
}
