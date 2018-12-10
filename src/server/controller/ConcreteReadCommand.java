package server.controller;

import interfaces.Command;
import interfaces.Event;

public class ConcreteReadCommand implements Command {
    private ConcreteReceiver receiver;
    private Event eventsContainer;

    public ConcreteReadCommand(ConcreteReceiver receiver){
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        eventsContainer = receiver.readMessage();
    }

    public Event getMessage(){
        return eventsContainer;
    }
}
