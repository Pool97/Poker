package server.controller;

import interfaces.Event;
import interfaces.Observable;
import interfaces.Observer;
import interfaces.Receiver;
import server.events.ChatMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ChatThread implements Receiver, Runnable, Observable {
    private BlockingQueue<ChatMessage> messageQueue;
    private Set<Observer> chatObservers;

    public ChatThread(BlockingQueue<ChatMessage> messageQueue){
        this.messageQueue = messageQueue;
        chatObservers = new HashSet<>();
    }

    @Override
    public void run() {
        while(true){
            notifyObservers(readMessage());
        }
    }

    @Override
    public void sendMessage(Event eventsContainer) {
        notifyObservers(eventsContainer);
    }

    @Override
    public Event readMessage(){
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void register(Observer observer) {
        chatObservers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        chatObservers.remove(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        chatObservers.forEach(observer -> observer.update(event));
    }
}
