package interfaces;

import server.events.*;

public interface EventsManager {
    void process(PlayerTurnEvent event);

    void process(BlindsUpdatedEvent event);

    void process(PlayerLoggedEvent event);

    void process(PlayerUpdatedEvent event);

    void process(PotUpdatedEvent event);

    void process(RoomCreatedEvent event);

    void process(CommunityUpdatedEvent event);

    void process(TurnStartedEvent event);

    void process(TurnEndedEvent event);

    void process(ShowdownEvent event);

    void process(PlayerHasLostEvent event);

    void process(PlayerHasWinEvent event);

    void process(PlayerFoldedEvent event);
}
