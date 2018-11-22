package server.automa;

import interfaces.BettingManager;
import server.controller.Context;
import server.events.EventsContainer;
import server.events.PlayerFoldedEvent;
import server.events.PlayerUpdatedEvent;
import server.events.PotUpdatedEvent;
import server.model.PlayerModel;
import server.model.actions.*;

public class ConcreteBettingManager implements BettingManager {
    private Context match;
    private PlayerModel player;

    public ConcreteBettingManager(Context match, PlayerModel player) {
        this.match = match;
        this.player = player;
    }

    @Override
    public void process(Fold fold) {
        match.getRoom().sendBroadcast(new EventsContainer(new PlayerFoldedEvent(player.getNickname())));
    }

    @Override
    public void process(Check check) {
        match.getRoom().sendBroadcast(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(),
                player.getChips(), check.getClass().getSimpleName()), new PotUpdatedEvent(match.getTurnModel().getPot())));
    }

    @Override
    public void process(Raise raise) {
        String value = Integer.toString(raise.getValue());
        match.getRoom().sendBroadcast(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(), player.getChips(),
                raise.getClass().getSimpleName() + " " + value), new PotUpdatedEvent(match.getTurnModel().getPot())));
    }

    @Override
    public void process(Call call) {
        String value = Integer.toString(call.getValue());
        match.getRoom().sendBroadcast(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(), player.getChips(),
                call.getClass().getSimpleName() + " " + value), new PotUpdatedEvent(match.getTurnModel().getPot())));
    }

    @Override
    public void process(Bet bet) {
        String value = Integer.toString(bet.getValue());
        match.getRoom().sendBroadcast(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(), player.getChips(),
                bet.getClass().getSimpleName() + " " + value), new PotUpdatedEvent(match.getTurnModel().getPot())));
    }
}
