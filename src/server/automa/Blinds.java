package server.automa;

import events.Events;
import events.PlayerUpdatedEvent;
import events.PotUpdatedEvent;
import interfaces.PokerState;
import javafx.util.Pair;
import server.model.*;

import java.util.concurrent.CountDownLatch;

public class Blinds implements PokerState {
    private Match match;

    public Blinds(Match match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        Events events = new Events();
        MatchModel matchModel = match.getMatchModel();
        TurnModel turnModel = match.getTurnModel();
        Room room = match.getRoom();

        PlayerModel smallBlind = room.getPlayer(Position.SB);
        smallBlind.addAction(new Pair<>(ActionType.SB, matchModel.getSmallBlind()));
        events.addEvent(new PlayerUpdatedEvent(smallBlind));
        turnModel.increasePot(matchModel.getSmallBlind());
        events.addEvent(new PotUpdatedEvent(turnModel.getPot()));
        room.sendBroadcast(new CountDownLatch(1), events);
        events.removeAll();

        PlayerModel player = room.getPlayer(Position.BB);
        player.addAction(new Pair<>(ActionType.BB, matchModel.getBigBlind()));
        events.addEvent(new PlayerUpdatedEvent(player));
        turnModel.increasePot(matchModel.getBigBlind());
        events.addEvent(new PotUpdatedEvent(turnModel.getPot()));
        room.sendBroadcast(new CountDownLatch(1), events);
        Action action = new Action(match);
        action.setTransitionStrategy(() -> match.setState(new Flop(match)));
        match.setState(new Action(match));
    }
}
