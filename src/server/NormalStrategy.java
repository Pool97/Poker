package server;

import events.*;
import interfaces.Strategy;
import server.model.PlayerModel;
import server.model.Room;
import server.model.TurnModel;
import server.socket.ServerManager;

import java.util.concurrent.CountDownLatch;

public class NormalStrategy implements Strategy {
    private TurnModel turnModel;
    private ServerManager server;
    private PlayerModel player;

    public NormalStrategy(TurnModel turnModel, ServerManager server, PlayerModel player) {
        this.turnModel = turnModel;
        this.server = server;
        this.player = player;
    }

    public int doAction() {
        Events events = new Events();
        Room room = server.getRoom();
        events.addEvent(new PositionChangedEvent(player.getPosition()));
        server.sendMessage(room.getConnections(), new CountDownLatch(1), events);
        events.removeAll();
        ActionPerformedEvent playerAction = server.listenForAMessage(room.getPlayerSocket(player));
        player.addToTurnActions(playerAction.getAction());
        turnModel.increasePot(playerAction.getAction().getStakeChips());
        events.addEvent(new PlayerUpdatedEvent(player));
        events.addEvent(new PotUpdatedEvent(turnModel.getPot()));
        server.sendMessage(room.getPlayerSocket(player), new CountDownLatch(1), events);
        return player.sumChipsBetted();
    }
}
