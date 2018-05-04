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
    protected Void doInBackground() throws IOException, ClassNotFoundException {
        T messageObject;
        do {
            ClientManager.logger.info(WAITING);
            messageObject = (T) inputStream.readObject();
            System.out.println(messageObject.getClass());
            if (messageObject instanceof Events) {
                Events events = (Events) messageObject;
                events.getEvents().forEach(event -> event.accept(processor));
                publish(messageObject);
            }
        } while (true);
    }

    @Override
    protected void process(List<T> chunks) {

    }

    public void setEventProcess(EventProcess processor) {
        this.processor = processor;
    }
}
