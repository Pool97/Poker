package server.model.automa;

import client.events.ActionPerformedEvent;
import interfaces.BettingManager;
import server.events.EventsContainer;
import server.events.PlayerTurnEvent;
import server.model.PlayerModel;
import server.model.actions.ActionBuilderFacade;
import server.model.actions.Fold;

public abstract class BettingRound extends AbstractPokerState {
    private PlayerTurnEvent optionsEvent;

    protected void doAction(PlayerModel player, Game game) {
        prepareActionsFor(player, game.getBigBlind());
        assignActionsTo(player);

        game.sendMessage(new EventsContainer(optionsEvent));

        ActionPerformedEvent playerAction = (ActionPerformedEvent) game.readMessage(player.getNickname()).getEvent();

        if (playerAction != null) {
            if(playerAction.getAction() instanceof Fold)
                player.setFolded(true);

            dealer.collectAction(player, playerAction.getAction().getValue());

            if(table.getPlayerByName(player.getNickname()).getChips() == 0)
                player.setAllIn(true);

            BettingManager bettingManager = new ConcreteBettingManager(game, table, player);
            playerAction.getAction().process(bettingManager);
        }
    }

    private void prepareActionsFor(PlayerModel player, int bigBlind) {
        ActionBuilderFacade builderFacade = new ActionBuilderFacade(player.getChips(), dealer.getPotMatchingValue(player.getNickname(), bigBlind), bigBlind);
        optionsEvent = builderFacade.buildActions(dealer.isBetPossible(table.getPlayers()));
    }

    private void assignActionsTo(PlayerModel player) {
        optionsEvent.setPlayerNickname(player.getNickname());
    }

    protected abstract boolean turnFinished(int cursor);

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
