package client.event;

import interfaces.EventManager;
import server.events.*;

public class EventsAdapter implements EventManager {
    @Override
    public void process(PlayerRound event) {

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

    @Override
    public void process(ChatNotify event) {

    }

    @Override
    public void process(NullEvent event) {

    }

    @Override
    public void process(MatchCanStart event) {

    }

    @Override
    public void process(ActionPerformed event) {

    }

    @Override
    public void process(MatchMode event) {

    }

    @Override
    public void process(PlayerConnected event) {

    }
}
