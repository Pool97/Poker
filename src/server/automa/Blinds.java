package server.automa;

import events.Events;
import events.PlayerUpdatedEvent;
import events.PotUpdatedEvent;
import interfaces.PokerState;
import javafx.util.Pair;
import server.model.*;
import server.socket.ServerManager;

import java.util.concurrent.CountDownLatch;

public class Blinds implements PokerState {
    private Match match;

    public Blinds(Match match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        Events events = new Events();
        ServerManager serverManager = match.getServerManager();
        MatchModel matchModel = match.getMatchModel();
        TurnModel turnModel = match.getTurnModel();
        Room room = serverManager.getRoom();

        PlayerModel smallBlind = room.getPlayerByPosition(Position.SB);
        turnModel.addAction(smallBlind, new Pair<>(ActionType.SB, matchModel.getSmallBlind()));
        events.addEvent(new PlayerUpdatedEvent(smallBlind));
        turnModel.increasePot(matchModel.getSmallBlind());
        events.addEvent(new PotUpdatedEvent(turnModel.getPot()));
        serverManager.sendMessage(room.getConnections(), new CountDownLatch(1), events);
        events.removeAll();

        PlayerModel player = room.getPlayerByPosition(Position.BB);
        turnModel.addAction(player, new Pair<>(ActionType.BB, matchModel.getBigBlind()));
        events.addEvent(new PlayerUpdatedEvent(player));
        turnModel.increasePot(matchModel.getBigBlind());
        events.addEvent(new PotUpdatedEvent(turnModel.getPot()));
        serverManager.sendMessage(room.getConnections(), new CountDownLatch(1), events);

        match.setState(new Action(match));
    }
}
