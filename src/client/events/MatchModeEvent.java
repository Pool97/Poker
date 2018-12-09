package client.events;

import interfaces.ClientEvent;

public class MatchModeEvent implements ClientEvent {
    private boolean fixedLimit;

    public MatchModeEvent(boolean fixedLimit){
        this.fixedLimit = fixedLimit;
    }

    public boolean isFixedLimit(){
        return fixedLimit;
    }
}
