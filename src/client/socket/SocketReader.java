package client.socket;

import events.Events;
import interfaces.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class SocketReader<T extends Message> extends SwingWorker<Void, T> {
    private ObjectInputStream inputStream;
    private EventProcessor processor;

    public SocketReader(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
        processor = new EventProcessor();
    }

    @Override
    protected Void doInBackground() throws IOException, ClassNotFoundException {
        T messageObject = null;
        System.out.println("Entrato");
        do {
            System.out.println("Sto aspettando");
            messageObject = (T) inputStream.readObject();
            if (messageObject instanceof Events) {
                Events events = (Events) messageObject;
                events.getEvents().forEach(event -> event.accept(processor));
            }
            publish(messageObject);
        } while (true);
    }

    @Override
    protected void process(List<T> chunks) {
        super.process(chunks);
    }
}
