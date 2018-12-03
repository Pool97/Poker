package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class PlayerLoggedEvent implements ServerEvent {
    private String nickname;
    private String avatar;
    private String position;
    private int chips;

    public PlayerLoggedEvent(String nickname, String avatar, String position, int chips) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.position = position;
        this.chips = chips;
    }

    public PlayerLoggedEvent(String nickname, String avatar) {
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public int getChips() {
        return chips;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
