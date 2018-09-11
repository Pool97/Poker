package server.controller;

import server.events.EventsContainer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class StreamHandler {
    private final static String STREAM_CREATION_ERROR = "Errore nella creazione degli stream... ";
    private final static String STREAM_ERROR = "Errore avvenuto nello stream... ";
    private Socket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    public StreamHandler(Socket socket) {
        this.socket = socket;
        createIOStream();
    }

    private void createIOStream() {
        try {
            outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.flush();
            inStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            MatchHandler.logger.info(STREAM_CREATION_ERROR);
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void sendMessage(EventsContainer message) {
        try {
            outStream.writeObject(message);
            outStream.flush();
        } catch (IOException e) {
            MatchHandler.logger.info(STREAM_ERROR);
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private EventsContainer readMessage() throws IOException, ClassNotFoundException {
        EventsContainer messageObject = null;
        messageObject = (EventsContainer) inStream.readObject();

        return messageObject;
    }

    public Runnable createRequestSender(EventsContainer message) {
        return () -> sendMessage(message);
    }

    public Callable<EventsContainer> createRequestHandler() {
        return this::readMessage;
    }
}