package client.socket;

import events.*;
import interfaces.EventProcess;

public class EventProcessor implements EventProcess {

    public EventProcessor() {
    }

    @Override
    public void process(ActionOptionsEvent event) {
        System.out.println("ActionOptionsEvent!");
    }

    @Override
    public void process(BlindsUpdatedEvent event) {
        System.out.println("BlindsUpdatedEvent!");
    }

    @Override
    public void process(PlayerAddedEvent event) {
        System.out.println("PlayerAddedEvent!");
    }

    @Override
    public void process(PlayerUpdatedEvent event) {
        System.out.println("PlayerUpdatedEvent!");
    }

    @Override
    public void process(PotUpdatedEvent event) {
        System.out.println("PotUpdatedEvent!");
    }
}
