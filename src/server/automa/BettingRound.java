package server.automa;

import client.events.ActionPerformedEvent;
import interfaces.BettingManager;
import interfaces.PokerState;
import server.controller.Context;
import server.controller.PlayersStateAnalyzer;
import server.controller.TurnActionsAnalyzer;
import server.events.EventsContainer;
import server.events.PlayerTurnEvent;
import server.model.PlayerModel;
import server.model.Position;
import server.model.actions.ActionBuilderFacade;

public abstract class BettingRound implements PokerState {
    private final static String ACTION_PERFORMED = "Puntata effettuata da: ";
    private final static String PLAYER_CHIPS = "Le Chips attuali di: ";
    private final static String PLAYER_OPTIONS = "Propongo al player le opzioni disponibili per la puntata... \n";
    private final static String PLAYERS_INFO = "Informo tutti i players della puntata effettuata... \n";

    protected Context match;
    private PlayerTurnEvent optionsEvent;
    protected PlayersStateAnalyzer playersAnalyzer;
    protected TurnActionsAnalyzer actionsAnalyzer;

    public BettingRound(Context match) {
        this.match = match;
        playersAnalyzer = new PlayersStateAnalyzer(match.getRoom().getControllers());
        actionsAnalyzer = new TurnActionsAnalyzer();
    }

    protected void doAction(PlayerModel player) {
        prepareActionsFor(player);
        assignActionsTo(player);

        sendPossibleActions();

        ActionPerformedEvent playerAction = readActionFrom(player);

        if (playerAction != null) {
            player.addAction(playerAction.getAction());
            updatePotBy(playerAction.getAction().getValue());
            BettingManager bettingManager = new ConcreteBettingManager(match, player);
            playerAction.getAction().process(bettingManager);
        }
    }

    private void prepareActionsFor(PlayerModel player) {
        ActionBuilderFacade builderFacade = new ActionBuilderFacade(player.getChips(), getCallValueFor(player), match.getMatchModel().getBigBlind());
        optionsEvent = builderFacade.buildActions(actionsAnalyzer.hasBetHappenedYet(playersAnalyzer.getPlayers()));
    }

    private void assignActionsTo(PlayerModel player) {
        optionsEvent.setPlayerNickname(player.getNickname());
    }

    private void sendPossibleActions() {
        match.getRoom().sendBroadcast(new EventsContainer(optionsEvent));
    }

    protected abstract boolean turnFinished(Position nextPosition);

    private ActionPerformedEvent readActionFrom(PlayerModel player) {
        EventsContainer actionEvent = match.getRoom().readMessage(player);
        if (!actionEvent.isEmpty()) {
            return (ActionPerformedEvent) actionEvent.getEvent();
        }
        return null;
    }

    private void updatePotBy(int value) {
        match.getTurnModel().increasePot(value);
    }

    protected boolean checkForActingPlayer() {
        return playersAnalyzer.getActivePlayers()
                .stream()
                .anyMatch(player -> player.getTurnBet() < actionsAnalyzer.maxBetAmong(playersAnalyzer.getAllInPlayers()));
    }

    protected boolean isMatched() {
        if (checkForActingPlayer())
            return false;

        return actionsAnalyzer.countDistinctBets(playersAnalyzer.getActivePlayers()) <= 1;
    }

    private int getMaxBet() {
        return actionsAnalyzer.maxBetAmong(playersAnalyzer.getPlayers());
    }

    private int getCallValueFor(PlayerModel player) {
        return getMaxBet() - player.getTurnBet();
    }
}
