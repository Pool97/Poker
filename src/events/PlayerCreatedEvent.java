package events;

import interfaces.Event;

import java.io.Serializable;

public class PlayerCreatedEvent implements Event, Serializable {
    private String nickname;
    private String avatar;

    public PlayerCreatedEvent(String nickname, String avatar) {
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }
}
