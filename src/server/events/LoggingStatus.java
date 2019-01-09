package server.events;

import interfaces.EventManager;
import interfaces.ServerEvent;

public class LoggingStatus implements ServerEvent {
    private boolean logPermission;

    public LoggingStatus(boolean logPermission) {
        this.logPermission = logPermission;
    }

    public boolean isPermitted() {
        return logPermission;
    }

    @Override
    public void accept(EventManager processor) {

    }
}
