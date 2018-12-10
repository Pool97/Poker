package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class PlayerHasWin implements ServerEvent {
    public String nickname;

    public PlayerHasWin(String nickname) {
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
