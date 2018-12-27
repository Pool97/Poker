package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class PlayerLogged implements ServerEvent {
    private String nickname;
    private String avatar;
    private int chips;

    public PlayerLogged(String nickname, String avatar, int chips) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.chips = chips;
    }

    public PlayerLogged(String nickname, String avatar) {
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

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }
}
