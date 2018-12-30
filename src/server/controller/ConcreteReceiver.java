package server.controller;

import client.events.PlayerConnected;
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

    public ConcreteReceiver(BlockingQueue<Event> readQueue){
        eventObservers = new HashSet<>();
        this.readQueue = readQueue;
    }

    @Override
    public void sendMessage(Event eventsContainer) {
        notifyObservers(eventsContainer);
    }

    @Override
    public Event readMessage() {
        try {
            Event event = readQueue.take();
            if(event instanceof PlayerConnected) {
                nickname = ((PlayerConnected) event).getNickname();
            }
            return event;
        } catch (InterruptedException e) {
            System.out.println("DISCONNESSO");
            return new PlayerDisconnected(nickname);
        }
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
