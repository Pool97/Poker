package server.controller;

import client.events.MatchCanStart;
import interfaces.Event;
import interfaces.Observer;
import server.events.ChatMessage;
import server.events.PlayerDisconnected;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class ClientSocket implements Runnable, Observer {
    private Socket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private CountDownLatch latch;
    private BlockingQueue<ChatMessage> messageQueue;
    private BlockingQueue<Event> writeQueue;

    private final static String STREAM_CREATION_ERROR = "Errore nella creazione degli stream... ";
    private final static String STREAM_ERROR = "Errore avvenuto nello stream... ";

    public ClientSocket(Socket socket, BlockingQueue<Event> writeQueue, BlockingQueue<ChatMessage> messageQueue, CountDownLatch latch){
        this.socket = socket;
        this.writeQueue = writeQueue;
        this.latch = latch;
        this.messageQueue = messageQueue;
        createIOStream();
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
        }
        return new PlayerDisconnected();
    }

    public Event readQueue(){
        try {
            return writeQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                Event event = readMessage();
                if(event instanceof MatchCanStart) {
                    latch.countDown();
                }else if(event instanceof ChatMessage){
                    messageQueue.put((ChatMessage)event);
                }else {
                    if(event != null)
                        writeQueue.put(event);
                    else {
                        return;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("DISCONNESSO");
            }
        }
    }

    @Override
    public void update(Event event) {
        sendMessage(event);
    }
}
