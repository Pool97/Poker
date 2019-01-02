package client.event;

import interfaces.ClientEvent;
import interfaces.EventManager;

public class MatchMode implements ClientEvent {
    private boolean fixedLimit;

    public MatchMode(boolean fixedLimit){
        this.fixedLimit = fixedLimit;
    }

    public boolean isFixedLimit(){
        return fixedLimit;
    }

    @Override
    public void accept(EventManager processor) {
        processor.process(this);
    }
}
