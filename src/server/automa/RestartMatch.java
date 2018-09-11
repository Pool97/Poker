package server.automa;

import client.events.CreatorRestartEvent;
import interfaces.PokerState;
import server.controller.MatchHandler;
import server.controller.PlayerController;
import server.controller.Room;
import server.events.EventsContainer;
import server.events.RestartMatchEvent;
import server.events.ServerClosedEvent;

public class RestartMatch implements PokerState {
    private MatchHandler match;

    public RestartMatch(MatchHandler match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        Room room = match.getRoom();
        PlayerController creator = room.getRoomCreator();
        room.sendTo(new EventsContainer(new RestartMatchEvent()), creator);
        CreatorRestartEvent creatorEvent = (CreatorRestartEvent) room.readMessage(creator).getEvent();
        if (creatorEvent.hasRestart()) {
            room.assignInitialState();
            room.setPlayersChips(0);
            match.setState(new StartGame(match));
        } else {
            System.out.println("Sono entrato qui pezzo di merda!!");
            room.sendBroadcastToLostPlayers(new EventsContainer(new ServerClosedEvent()));
            room.sendBroadcast(new EventsContainer(new ServerClosedEvent())); //chiudi tutta la vita
            match.stop.countDown();
        }

    }
}
