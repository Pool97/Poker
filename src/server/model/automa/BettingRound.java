package server.model.automa;

import client.events.ActionPerformedEvent;
import interfaces.BettingManager;
import server.model.PlayersStateAnalyzer;
import server.events.EventsContainer;
import server.events.PlayerTurnEvent;
import server.model.PlayerModel;
import server.model.Position;
import server.model.actions.ActionBuilderFacade;
import server.model.actions.AllIn;
import server.model.actions.Fold;

public abstract class BettingRound extends AbstractPokerState {
    private PlayerTurnEvent optionsEvent;
    protected PlayersStateAnalyzer playersAnalyzer;

    public BettingRound() {
        playersAnalyzer = new PlayersStateAnalyzer(table.getPlayers());
    }

    protected void doAction(PlayerModel player, Game game) {
        prepareActionsFor(player);
        assignActionsTo(player);

        game.sendMessage(new EventsContainer(optionsEvent));

        ActionPerformedEvent playerAction = (ActionPerformedEvent) game.readMessage(player.getNickname()).getEvent();

        if (playerAction != null) {
            if(playerAction.getAction() instanceof AllIn){
                player.setAllIn(true);
            }else if(playerAction.getAction() instanceof Fold){
                player.setFolded(true);
            }
            dealer.collectAction(player, playerAction.getAction().getValue());
            BettingManager bettingManager = new ConcreteBettingManager(game, table, player);
            playerAction.getAction().process(bettingManager);
        }
    }

    private void prepareActionsFor(PlayerModel player) {
        ActionBuilderFacade builderFacade = new ActionBuilderFacade(player.getChips(), dealer.getPotMatchingValue(player.getNickname()), dealer.getBigBlind());
        optionsEvent = builderFacade.buildActions(dealer.isBetPossible(playersAnalyzer.getPlayers()));
    }

    private void assignActionsTo(PlayerModel player) {
        optionsEvent.setPlayerNickname(player.getNickname());
    }

    protected abstract boolean turnFinished(Position nextPosition);

    protected boolean checkForActingPlayer() {
        return playersAnalyzer.getActivePlayers()
                .stream()
                .anyMatch(player -> dealer.getTurnBetOf(player.getNickname()) < dealer.maxBetAmong(playersAnalyzer.getAllInPlayers()));
    }

    protected boolean isMatched() {
        if (checkForActingPlayer())
            return false;

        return dealer.isBetPossible(playersAnalyzer.getActivePlayers());
    }
}
