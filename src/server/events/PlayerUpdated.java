package server.events;

import interfaces.EventManager;
import interfaces.ServerEvent;

public class PlayerUpdated implements ServerEvent {
    private String nickname;
    private int chips;
    private String action;
    private int value;

    public PlayerUpdated(String nickname, int chips, String action, int value) {
        this.nickname = nickname;
        this.chips = chips;
        this.action = action;
        this.value = value;
    }

    public String getNickname() {
        return nickname;
    }

    public int getChips() {
        return chips;
    }

    public String getAction() {
        return action;
    }

    public int getValue(){
        return value;
    }

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
