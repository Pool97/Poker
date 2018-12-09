package client.net;

import interfaces.EventsManager;
import interfaces.ServerEvent;
import server.events.EventsContainer;

import javax.swing.*;
import java.util.List;

public class ReadServerMessagesTask extends SwingWorker<Void, EventsContainer> {
    private final static String WAITING_FOR_SERVER = "In attesa di un messaggio dal Server...";
    private EventsManager eventsManager;

    public ReadServerMessagesTask() {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Void doInBackground() throws InterruptedException {
        EventsContainer eventsContainer;
        do {
            Client.logger.info(WAITING_FOR_SERVER);
            eventsContainer = (EventsContainer) Client.getInstance().readMessage();
            publish(eventsContainer);
            Thread.sleep(800);
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
