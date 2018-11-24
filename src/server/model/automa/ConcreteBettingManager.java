package server.model.automa;

import interfaces.BettingManager;
import server.events.EventsContainer;
import server.events.PlayerFoldedEvent;
import server.events.PlayerUpdatedEvent;
import server.events.PotUpdatedEvent;
import server.model.PlayerModel;
import server.model.Table;
import server.model.actions.*;

public class ConcreteBettingManager implements BettingManager {
    private Table table;
    private PlayerModel player;

    public ConcreteBettingManager(Table table, PlayerModel player) {
        this.player = player;
        this.table = table;
    }

    @Override
    public void process(Fold fold) {
        table.sendBroadcast(new EventsContainer(new PlayerFoldedEvent(player.getNickname())));
    }

    @Override
    public void process(Check check) {
        table.sendBroadcast(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(),
                player.getChips(), check.getClass().getSimpleName()), new PotUpdatedEvent(table.getPot())));
    }

    @Override
    public void process(Raise raise) {
        String value = Integer.toString(raise.getValue());
        table.sendBroadcast(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(), player.getChips(),
                raise.getClass().getSimpleName() + " " + value), new PotUpdatedEvent(table.getPot())));
    }

    @Override
    public void process(Call call) {
        String value = Integer.toString(call.getValue());
        table.sendBroadcast(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(), player.getChips(),
                call.getClass().getSimpleName() + " " + value), new PotUpdatedEvent(table.getPot())));
    }

    @Override
    public void process(Bet bet) {
        String value = Integer.toString(bet.getValue());
        table.sendBroadcast(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(), player.getChips(),
                bet.getClass().getSimpleName() + " " + value), new PotUpdatedEvent(table.getPot())));
    }
}
