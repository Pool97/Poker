package server.controller;

import client.event.*;
import interfaces.ClientEvent;
import interfaces.Event;
import interfaces.Observable;
import interfaces.Observer;
import server.events.ChatMessage;
import server.events.PlayerDisconnected;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;

public class ClientSocket implements Runnable, Observer, Observable {
    private Socket socket;
    private String nickname;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private BlockingQueue<ChatMessage> messageQueue;
    private BlockingQueue<Event> writeQueue;
    private HashSet<Observer> observers;
    private SocketEventManager eventManager;

    private final static String STREAM_CREATION_ERROR = "Errore nella creazione degli stream... ";
    private final static String STREAM_ERROR = "Errore avvenuto nello stream... ";

    public ClientSocket(Socket socket, BlockingQueue<Event> writeQueue, BlockingQueue<ChatMessage> messageQueue){
        this.socket = socket;
        this.writeQueue = writeQueue;
        this.messageQueue = messageQueue;
        observers = new HashSet<>();
        createIOStream();
        eventManager = new SocketEventManager();
    }

    private void createIOStream() {
        try {
            outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.flush();
            inStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Game.logger.info(STREAM_CREATION_ERROR);
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void sendMessage(Event message) {
        try {
            outStream.writeObject(message);
            outStream.flush();
        } catch (IOException e) {
            Game.logger.info(STREAM_ERROR);
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private Event readMessage(){
        try {
            return (Event) inStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Thread.currentThread().interrupt();
            handle();
        }
        return null;
    }

    @Override
    public void run() {
        while(!socket.isClosed()){
            Event event = readMessage();
            if(event != null)
                ((ClientEvent)event).accept(eventManager);
            else {
                return;
            }
        }
    }

    public void close() {
        try {
            inStream.close();
            outStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handle() {
        notifyObservers(new PlayerDisconnected(nickname));
        close();
    }

    @Override
    public void update(Event event) {
        sendMessage(event);
    }

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        observers.forEach(observer -> observer.update(event));
    }

    class SocketEventManager extends EventsAdapter{
        @Override
        public void process(MatchCanStart event) {
            notifyObservers(event);
        }

        @Override
        public void process(ChatMessage event) {
            try {
                messageQueue.put(event);
            } catch (InterruptedException e) {
                handle();
            }
        }

        @Override
        public void process(ActionPerformed event) {
            try {
                writeQueue.put(event);
            } catch (InterruptedException e) {
                handle();
            }
        }

        @Override
        public void process(MatchMode event) {
            notifyObservers(event);
        }

        @Override
        public void process(PlayerConnected event) {
            nickname = event.getNickname();
            try {
                writeQueue.put(event);
                notifyObservers(event);
            } catch (InterruptedException e) {
                handle();
            }
        }
    }
}
