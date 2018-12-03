package server.model.automa;

import client.events.ActionPerformedEvent;
import interfaces.BettingManager;
import server.events.EventsContainer;
import server.events.PlayerRoundEvent;
import server.model.PlayerModel;
import server.model.actions.BetNoLimit;
import server.model.actions.Fold;
import server.model.actions.RaiseNoLimit;
import server.model.gamestructure.NoLimitActionGenerator;

import java.util.AbstractMap;

public abstract class NoLimitBettingRound extends AbstractPokerState {
    private PlayerRoundEvent optionsEvent;


    protected void doAction(PlayerModel player, Game game) {
        optionsEvent = new PlayerRoundEvent(player.getNickname());
        int currentBet = dealer.getPotMatchingValue(player.getNickname(), game.getBigBlind());

        NoLimitActionGenerator generator = new NoLimitActionGenerator(dealer.getMinimumLegalRaise(), player,
                currentBet);

        optionsEvent.addOption(generator.retrieveCall());
        optionsEvent.addOption(generator.retrieveCheck());
        optionsEvent.addOption(generator.retrieveFold());
        optionsEvent.addOption(generator.retrieveBet());
        optionsEvent.addOption(generator.retrieveRaise());
        optionsEvent.addOption(generator.retrieveAllIn());

        optionsEvent.setPlayerNickname(player.getNickname());
        optionsEvent.getOptions().forEach(System.out::println);

        game.sendMessage(new EventsContainer(optionsEvent));

        ActionPerformedEvent playerAction = (ActionPerformedEvent) game.readMessage(player.getNickname()).getEvent();

        if (playerAction != null) {
            if(playerAction.getAction() instanceof Fold)
                player.setFolded(true);

            if(playerAction.getAction() instanceof BetNoLimit)
                dealer.setMinimumLegalRaise(new AbstractMap.SimpleEntry<>(player.getNickname(), playerAction.getAction().getValue()));

            if(playerAction.getAction() instanceof RaiseNoLimit)
                dealer.setMinimumLegalRaise(new AbstractMap.SimpleEntry<>(player.getNickname(), playerAction.getAction().getValue() - currentBet));

            dealer.collectAction(player, playerAction.getAction().getValue());

            if(table.getPlayerByName(player.getNickname()).getChips() == 0)
                player.setAllIn(true);

            BettingManager bettingManager = new ConcreteBettingManager(game, table, player);
            playerAction.getAction().process(bettingManager);
        }
    }

    protected abstract boolean roundFinished(int cursor);

    protected boolean checkForActingPlayer() {
        return table.getActivePlayers()
                .stream()
                .anyMatch(player -> dealer.getTurnBetOf(player.getNickname()) < dealer.maxBetAmong(table.getAllInPlayers()));
    }

    protected boolean isMatched() {
        if (checkForActingPlayer())
            return false;

        return dealer.isBetPossible(table.getActivePlayers());
    }
}
