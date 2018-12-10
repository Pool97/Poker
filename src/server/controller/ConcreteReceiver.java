package server.controller;

import interfaces.Event;
import interfaces.Observable;
import interfaces.Observer;
import interfaces.Receiver;
import server.events.ChatMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ConcreteReceiver implements Receiver, Observable {
    private String nickname;
    private Set<Observer> observers;
    private ArrayList<ChatMessage> messages;
    private BlockingQueue<Event> readQueue;
    private BlockingQueue<Event> writeQueue;

    public ConcreteReceiver(String nickname, BlockingQueue<Event> readQueue, BlockingQueue<Event> writeQueue){
        observers = new HashSet<>();
        messages = new ArrayList<>();
        this.nickname = nickname;
        this.readQueue = readQueue;
        this.writeQueue = writeQueue;
    }

    @Override
    public void sendMessage(Event eventsContainer) {
        try {
            writeQueue.put(eventsContainer);
            notifyObservers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Event readMessage() {
        Event event;
        try {
             event = readQueue.take();
             if(event instanceof ChatMessage) {
                 messages.add((ChatMessage) event);
             }
             else
                 return event;
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
