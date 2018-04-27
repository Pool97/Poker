package server;

import events.Events;
import events.PlayerUpdatedEvent;
import events.PotUpdatedEvent;
import interfaces.Strategy;
import server.model.*;
import server.socket.ServerManager;

import java.util.concurrent.CountDownLatch;

public class SmallBlindStrategy implements Strategy {
    private TurnModel turnModel;
    private ServerManager server;

    public SmallBlindStrategy(TurnModel turnModel, ServerManager server) {
        this.turnModel = turnModel;
        this.server = server;
    }

    public int doAction() {
        Events events = new Events();
        Room room = server.getRoom();
        PlayerModel smallBlind = room.getPlayerByPosition(Position.SB);
        smallBlind.addToTurnActions(new StakeAction(ActionType.SB, turnModel.getSmallBlind()));
        events.addEvent(new PlayerUpdatedEvent(smallBlind));
        turnModel.increasePot(turnModel.getSmallBlind());
        events.addEvent(new PotUpdatedEvent(turnModel.getPot()));
        server.sendMessage(room.getConnections(), new CountDownLatch(1), events);
        return turnModel.getSmallBlind();
    }
}
