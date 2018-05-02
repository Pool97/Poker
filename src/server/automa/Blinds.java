package server.automa;

import events.Events;
import events.PlayerUpdatedEvent;
import events.PotUpdatedEvent;
import interfaces.PokerState;
import javafx.util.Pair;
import server.model.*;

public class Blinds implements PokerState {
    private Match match;

    public Blinds(Match match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        MatchModel matchModel = match.getMatchModel();
        TurnModel turnModel = match.getTurnModel();
        Room room = match.getRoom();

        PlayerModel smallBlind = room.getPlayer(Position.SB);
        smallBlind.addAction(new Pair<>(ActionType.SB, matchModel.getSmallBlind()));
        turnModel.increasePot(matchModel.getSmallBlind());
        room.sendBroadcast(new Events(new PlayerUpdatedEvent(smallBlind), new PotUpdatedEvent(turnModel.getPot())));

        PlayerModel player = room.getPlayer(Position.BB);
        player.addAction(new Pair<>(ActionType.BB, matchModel.getBigBlind()));
        turnModel.increasePot(matchModel.getBigBlind());
        room.sendBroadcast(new Events(new PlayerUpdatedEvent(player), new PotUpdatedEvent(turnModel.getPot())));

        Action action = new Action(match);
        action.setTransitionStrategy(() -> match.setState(new Flop(match)));
        match.setState(new Action(match));
    }
}
