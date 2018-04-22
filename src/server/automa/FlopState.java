package server.automa;

import interfaces.Observable;
import interfaces.Observer;
import interfaces.PokerState;
import interfaces.StateSwitcher;
import server.model.DeckModel;

import java.util.ArrayList;


public class FlopState implements PokerState, Observable {
    private ArrayList<Observer> observers;

    public FlopState(DeckModel deckModel) {
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

    @Override
    public void accept(StateSwitcher switcher) {

    }

    @Override
    public void start() {

    }
}
