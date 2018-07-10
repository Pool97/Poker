package server.events;

import interfaces.EventsManager;
import interfaces.PokerAction;
import interfaces.ServerEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerTurnEvent implements Serializable, ServerEvent {
    private ArrayList<PokerAction> optionsAvailable;

    public PlayerTurnEvent() {
        optionsAvailable = new ArrayList<>();
    }

    public void addOption(PokerAction option) {
        optionsAvailable.add(option);
    }

    public ArrayList<PokerAction> getOptions() {
        return optionsAvailable;
    }


    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
