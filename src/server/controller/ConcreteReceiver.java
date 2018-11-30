package server.controller;

import interfaces.Observable;
import interfaces.Observer;
import interfaces.Receiver;
import server.events.EventsContainer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ConcreteReceiver implements Receiver, Observable {
    private String nickname;
    private Set<Observer> observers;
    private BlockingQueue<EventsContainer> readQueue;
    private BlockingQueue<EventsContainer> writeQueue;

    public ConcreteReceiver(String nickname, BlockingQueue<EventsContainer> readQueue, BlockingQueue<EventsContainer> writeQueue){
        observers = new HashSet<>();
        this.nickname = nickname;
        this.readQueue = readQueue;
        this.writeQueue = writeQueue;
    }

    @Override
    public void sendMessage(EventsContainer eventsContainer) {
        try {
            writeQueue.put(eventsContainer);
            notifyObservers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EventsContainer readMessage() {
        try {
            return readQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNickname(){
        return nickname;
    }

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
