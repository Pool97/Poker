package server.controller;

import interfaces.Command;
import server.events.EventsContainer;

public class ConcreteSendCommand implements Command {
    private ConcreteReceiver receiver;
    private EventsContainer message;

    public ConcreteSendCommand(ConcreteReceiver receiver, EventsContainer message){
        this.receiver = receiver;
        this.message = message;

    }

    @Override
    public void execute() {
        receiver.sendMessage(message);
    }
}
