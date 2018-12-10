package server.controller;

import interfaces.Command;
import interfaces.Event;

public class ConcreteSendCommand implements Command {
    private ConcreteReceiver receiver;
    private Event message;

    public ConcreteSendCommand(ConcreteReceiver receiver, Event message){
        this.receiver = receiver;
        this.message = message;

    }

    @Override
    public void execute() {
        receiver.sendMessage(message);
    }
}
