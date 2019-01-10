package client.net;

import interfaces.Event;
import interfaces.EventManager;
import interfaces.ServerEvent;
import server.events.NullEvent;

import javax.swing.*;
import java.util.List;

public class ReadServerMessagesTask extends SwingWorker<Void, Event> {
    private final static String WAITING_FOR_SERVER = "In attesa di un messaggio dal Server...";
    private EventManager eventsManager;

    public ReadServerMessagesTask() {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Void doInBackground() throws InterruptedException {
        Event eventsContainer;
        do {
            Client.logger.info(WAITING_FOR_SERVER);
            eventsContainer = ClientWrapper.getInstance().readMessage();
            publish(eventsContainer);
            Thread.sleep(500);
        } while (!(eventsContainer instanceof NullEvent));
        return null;
    }

    @Override
    protected void process(List<Event> chunks) {
        if(chunks.size() > 0){
            Event eventsContainer = chunks.get(0);
            ((ServerEvent)eventsContainer).accept(eventsManager);
        }
    }

    public void setEventsManager(EventManager processor) {
        this.eventsManager = processor;
    }
}
