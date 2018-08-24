package client.socket;

import interfaces.EventsManager;
import interfaces.ServerEvent;
import server.events.Events;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class SocketReader extends SwingWorker<Void, Events> {
    private ObjectInputStream inputStream;
    private final static String WAITING_FOR_SERVER = "In attesa di un messaggio dal Server...";
    private EventsManager eventsManager;

    public SocketReader(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Void doInBackground() throws IOException, ClassNotFoundException, InterruptedException {
        Events events;
        do {
            ClientManager.logger.info(WAITING_FOR_SERVER);
            events = (Events) inputStream.readObject();
            publish(events);
            Thread.sleep(1500);

        } while (true);
    }

    @Override
    protected void process(List<Events> chunks) {
        Events events = chunks.get(0);
        events.getEvents().forEach(event -> ((ServerEvent) event).accept(eventsManager));
    }

    public void setEventsManager(EventsManager processor) {
        this.eventsManager = processor;
    }
}
