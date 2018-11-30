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
    private Game game;
    private PlayerModel player;
    private Table table;

    public ConcreteBettingManager(Game game, Table table, PlayerModel player) {
        this.player = player;
        this.game = game;
        this.table = table;
    }

    @Override
    public void process(Fold fold) {
        game.sendMessage(new EventsContainer(new PlayerFoldedEvent(player.getNickname())));
    }

    @Override
    public void process(Check check) {
        game.sendMessage(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(),
                player.getChips(), check.getClass().getSimpleName()), new PotUpdatedEvent(table.getPotValue())));
    }

    @Override
    public void process(Raise raise) {
        String value = Integer.toString(raise.getValue());
        game.sendMessage(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(), player.getChips(),
                raise.getClass().getSimpleName() + " " + value), new PotUpdatedEvent(table.getPotValue())));
    }

    @Override
    public void process(Call call) {
        String value = Integer.toString(call.getValue());
        game.sendMessage(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(), player.getChips(),
                call.getClass().getSimpleName() + " " + value), new PotUpdatedEvent(table.getPotValue())));
    }

    @Override
    public void process(Bet bet) {
        String value = Integer.toString(bet.getValue());
        game.sendMessage(new EventsContainer(new PlayerUpdatedEvent(player.getNickname(), player.getChips(),
                bet.getClass().getSimpleName() + " " + value), new PotUpdatedEvent(table.getPotValue())));
    }
}
