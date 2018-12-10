package client.net;

import interfaces.Event;
import interfaces.EventsManager;
import interfaces.ServerEvent;

import javax.swing.*;
import java.util.List;

public class ReadServerMessagesTask extends SwingWorker<Void, Event> {
    private final static String WAITING_FOR_SERVER = "In attesa di un messaggio dal Server...";
    private EventsManager eventsManager;

    public ReadServerMessagesTask() {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Void doInBackground() throws InterruptedException {
        Event eventsContainer;
        do {
            Client.logger.info(WAITING_FOR_SERVER);
            eventsContainer = (Event) Client.getInstance().readMessage();
            publish(eventsContainer);
            Thread.sleep(800);
        } while (true);
    }

    @Override
    protected void process(List<Event> chunks) {
        Event eventsContainer = chunks.get(0);
        ((ServerEvent)eventsContainer).accept(eventsManager);
    }

    public void setEventsManager(EventsManager processor) {
        this.eventsManager = processor;
    }
}
