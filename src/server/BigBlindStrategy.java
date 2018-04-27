package server;

import events.Events;
import events.PlayerUpdatedEvent;
import events.PotUpdatedEvent;
import interfaces.Strategy;
import server.model.*;
import server.socket.ServerManager;

import java.util.concurrent.CountDownLatch;

public class BigBlindStrategy implements Strategy {
    private TurnModel turnModel;
    private ServerManager server;

    public BigBlindStrategy(TurnModel turnModel, ServerManager server) {
        this.turnModel = turnModel;
        this.server = server;
    }

    public int doAction() {
        Events events = new Events();
        Room room = server.getRoom();
        PlayerModel player = room.getPlayerByPosition(Position.BB);
        player.addToTurnActions(new StakeAction(ActionType.BB, turnModel.getBigBlind()));
        events.addEvent(new PlayerUpdatedEvent(player));
        turnModel.increasePot(turnModel.getBigBlind());
        events.addEvent(new PotUpdatedEvent(turnModel.getPot()));
        server.sendMessage(room.getConnections(), new CountDownLatch(1), events);
        return turnModel.getBigBlind();
    }
}
