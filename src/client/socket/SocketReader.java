package client.socket;

import events.Events;
import interfaces.EventProcess;
import interfaces.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class SocketReader<T extends Message> extends SwingWorker<Void, T> {
    private ObjectInputStream inputStream;
    private final static String WAITING = "In attesa di un messaggio dal Server...";
    private EventProcess processor;

    public SocketReader(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Void doInBackground() throws IOException, ClassNotFoundException, InterruptedException {
        T messageObject;
        do {
            ClientManager.logger.info(WAITING);
            messageObject = (T) inputStream.readObject();
            if (messageObject instanceof Events) {
                publish(messageObject);
                Thread.sleep(1000);
            }
        } while (true);
    }

    @Override
    protected void process(List<T> chunks) {
        Events events = (Events) chunks.get(0);
        events.getEvents().forEach(event -> event.accept(processor));
    }

    public void setEventProcess(EventProcess processor) {
        this.processor = processor;
    }
}
