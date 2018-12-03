package server.events;

import interfaces.EventsManager;
import interfaces.PokerAction;
import interfaces.ServerEvent;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerRoundEvent implements Serializable, ServerEvent {
    private String player;
    private ArrayList<PokerAction> optionsAvailable;

    public PlayerRoundEvent(String player) {
        this.player = player;
        optionsAvailable = new ArrayList<>();
    }

    public void addOption(PokerAction option) {
        if(!optionsAvailable.contains(option))
            optionsAvailable.add(option);
    }

    public ArrayList<PokerAction> getOptions() {
        return optionsAvailable;
    }

    public String getPlayerNickname() {
        return player;
    }

    public void setPlayerNickname(String nickname) {
        player = nickname;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
