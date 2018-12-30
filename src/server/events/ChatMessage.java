package server.events;

import interfaces.EventManager;
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
    public void accept(EventManager processor) {
        processor.process(this);
    }

    public String toString(){
        return message;
    }
}
