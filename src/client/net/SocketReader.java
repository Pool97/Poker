package client.net;

import interfaces.EventsManager;
import interfaces.ServerEvent;
import server.events.EventsContainer;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class SocketReader extends SwingWorker<Void, EventsContainer> {
    private ObjectInputStream inputStream;
    private final static String WAITING_FOR_SERVER = "In attesa di un messaggio dal Server...";
    private EventsManager eventsManager;

    public SocketReader(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Void doInBackground() throws IOException, ClassNotFoundException, InterruptedException {
        EventsContainer eventsContainer;
        do {
            ClientManager.logger.info(WAITING_FOR_SERVER);
            eventsContainer = (EventsContainer) inputStream.readObject();
            publish(eventsContainer);
            Thread.sleep(1500);

        } while (true);
    }

    @Override
    protected void process(List<EventsContainer> chunks) {
        EventsContainer eventsContainer = chunks.get(0);
        eventsContainer.getEvents().forEach(event -> ((ServerEvent) event).accept(eventsManager));
    }

    public void setEventsManager(EventsManager processor) {
        this.eventsManager = processor;
    }
}
