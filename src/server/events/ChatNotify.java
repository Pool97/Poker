package server.events;

import interfaces.EventManager;
import interfaces.ServerEvent;

public class ChatNotify implements ServerEvent {
    private String serverMessage;

    public ChatNotify(String serverMessage){
        this.serverMessage = serverMessage;
    }

    public String getServerMessage(){
        return serverMessage;
    }

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
