package server.events;

import interfaces.EventsManager;
import interfaces.ServerEvent;

public class ChatMessage implements ServerEvent {
    private String nickname;
    private String message;

    public ChatMessage(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void accept(EventsManager processor) {
        processor.process(this);
    }

    public String toString(){
        return message;
    }
}
