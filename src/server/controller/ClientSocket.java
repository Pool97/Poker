package server.controller;

import interfaces.Observer;
import server.events.EventsContainer;
import server.model.automa.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientSocket implements Runnable, Observer {
    private Socket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private BlockingQueue<EventsContainer> readQueue;
    private BlockingQueue<EventsContainer> writeQueue;

    private final static String STREAM_CREATION_ERROR = "Errore nella creazione degli stream... ";
    private final static String STREAM_ERROR = "Errore avvenuto nello stream... ";

    public ClientSocket(Socket socket, BlockingQueue<EventsContainer> readQueue, BlockingQueue<EventsContainer> writeQueue){
        this.socket = socket;
        this.readQueue = readQueue;
        this.writeQueue = writeQueue;
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

    private void sendMessage(EventsContainer message) {
        try {
            outStream.writeObject(message);
            outStream.flush();
        } catch (IOException e) {
            Game.logger.info(STREAM_ERROR);
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private EventsContainer readMessage(){
        try {
            return (EventsContainer) inStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; //ritorna l'evento di disconnessione!
    }

    @Override
    public void run() {
        while(true){
            try {
                writeQueue.put(readMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update() {
        try {
            sendMessage(readQueue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
