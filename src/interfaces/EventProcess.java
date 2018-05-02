package interfaces;

import events.*;

public interface EventProcess {
    void process(ActionOptionsEvent event);

    void process(BlindsUpdatedEvent event);

    void process(PlayerAddedEvent event);

    void process(PlayerUpdatedEvent event);

    void process(PotUpdatedEvent event);

}
