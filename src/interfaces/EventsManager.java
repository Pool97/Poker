package interfaces;

import server.events.*;

public interface EventsManager {
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
}
