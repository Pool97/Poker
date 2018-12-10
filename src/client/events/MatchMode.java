package client.events;

import interfaces.ClientEvent;

public class MatchMode implements ClientEvent {
    private boolean fixedLimit;

    public MatchMode(boolean fixedLimit){
        this.fixedLimit = fixedLimit;
    }

    public boolean isFixedLimit(){
        return fixedLimit;
    }
}
