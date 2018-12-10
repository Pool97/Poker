package client.events;

import interfaces.EventsManager;
import server.events.*;

public class EventsAdapter implements EventsManager {
    @Override
    public void process(PlayerRound event) {

    }

    @Override
    public void process(BlindsUpdated event) {

    }

    @Override
    public void process(PlayerLogged event) {

    }

    @Override
    public void process(PlayerUpdated event) {

    }

    @Override
    public void process(PotUpdated event) {

    }

    @Override
    public void process(RoomCreated event) {

    }

    @Override
    public void process(CommunityUpdated event) {

    }

    @Override
    public void process(TurnStarted event) {

    }

    @Override
    public void process(TurnEnded event) {

    }

    @Override
    public void process(Showdown event) {

    }

    @Override
    public void process(MatchLost event) {

    }

    @Override
    public void process(PlayerHasWin event) {

    }

    @Override
    public void process(PlayerDisconnected event) {

    }

    @Override
    public void process(ServerClosed event) {

    }

    @Override
    public void process(ChatMessage event) {

    }
}
