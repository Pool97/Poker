package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class PlayerDisconnected implements ServerEvent {
    private String nickname;

    public PlayerDisconnected(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
