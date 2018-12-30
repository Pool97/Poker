package server.events;

import interfaces.ClientEvent;
import interfaces.EventManager;
import interfaces.ServerEvent;

public class PlayerDisconnected implements ClientEvent, ServerEvent {
    private String nickname;

    public PlayerDisconnected(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
