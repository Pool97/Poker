package interfaces;

import client.event.ActionPerformed;
import client.event.MatchCanStart;
import client.event.MatchMode;
import client.event.PlayerConnected;
import server.events.*;

public interface EventManager {
    void process(PlayerRound event);

    void process(PlayerLogged event);

    void process(PlayerUpdated event);

    void process(PotUpdated event);

    void process(RoomCreated event);

    void process(CommunityUpdated event);

    void process(TurnStarted event);

    void process(TurnEnded event);

    void process(Showdown event);

    void process(MatchLost event);

    void process(PlayerHasWin event);

    void process(PlayerDisconnected event);

    void process(ServerClosed event);

    void process(ChatMessage event);

    void process(ChatNotify event);

    void process(NullEvent event);

    void process(MatchCanStart event);

    void process(ActionPerformed event);

    void process(MatchMode event);

    void process(PlayerConnected event);
}
