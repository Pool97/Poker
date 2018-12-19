package server.controller;

import interfaces.Event;
import interfaces.Observable;
import interfaces.Observer;
import interfaces.Receiver;
import server.events.PlayerDisconnected;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ConcreteReceiver implements Receiver, Observable {
    private String nickname;
    private Set<Observer> eventObservers;
    private BlockingQueue<Event> readQueue;

    public ConcreteReceiver(String nickname, BlockingQueue<Event> readQueue){
        eventObservers = new HashSet<>();
        this.nickname = nickname;
        this.readQueue = readQueue;
    }

    @Override
    public void sendMessage(Event eventsContainer) {
        notifyObservers(eventsContainer);
    }

    @Override
    public Event readMessage() {
        try {
            return readQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("DISCONESSO");
        }

        return new PlayerDisconnected(nickname);
    }

    public String getNickname(){
        return nickname;
    }

    @Override
    public void register(Observer observer) {
        eventObservers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        eventObservers.remove(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        eventObservers.forEach(observer -> observer.update(event));
    }
}
