package server.controller;

import interfaces.Command;
import server.events.EventsContainer;

public class ConcreteReadCommand implements Command {
    private ConcreteReceiver receiver;
    private EventsContainer eventsContainer;

    public ConcreteReadCommand(ConcreteReceiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        eventsContainer = receiver.readMessage();
    }

    public EventsContainer getMessage(){
        return eventsContainer;
    }
}
