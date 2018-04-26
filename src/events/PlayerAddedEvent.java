package events;

import interfaces.Event;
import server.model.Position;

public class PlayerAddedEvent implements Event {
    private String nickname;
    private String avatar;
    private Position position;
    private int initialChips;

    public PlayerAddedEvent(String nickname, String avatar, Position position, int initialChips) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.position = position;
        this.initialChips = initialChips;
    }

    public int getInitialChips() {
        return initialChips;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public Position getPosition() {
        return position;
    }
}
