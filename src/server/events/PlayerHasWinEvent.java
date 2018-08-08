package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class PlayerHasWinEvent implements ServerEvent {
    public String nickname;

    public PlayerHasWinEvent(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }

    public String getNickname() {
        return nickname;
    }
}
