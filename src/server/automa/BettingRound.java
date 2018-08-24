package server.automa;

import client.events.ActionPerformedEvent;
import interfaces.PokerState;
import server.events.*;
import server.model.*;

public abstract class BettingRound implements PokerState {
    private final static String ACTION_PERFORMED = "Puntata effettuata da: ";
    private final static String PLAYER_CHIPS = "Le Chips attuali di: ";
    private final static String PLAYER_OPTIONS = "Propongo al player le opzioni disponibili per la puntata... \n";
    private final static String PLAYERS_INFO = "Informo tutti i players della puntata effettuata... \n";

    protected MatchHandler match;
    private PlayerTurnEvent optionsEvent;
    protected PlayersStateAnalyzer playersAnalyzer;
    protected TurnActionsAnalyzer actionsAnalyzer;

    public BettingRound(MatchHandler match) {
        this.match = match;
        playersAnalyzer = new PlayersStateAnalyzer(match.getRoom().getControllers());
        actionsAnalyzer = new TurnActionsAnalyzer();
    }

    protected void doAction(PlayerModel player) {
        Room room = match.getRoom();
        TurnModel turnModel = match.getTurnModel();

        prepareActionsFor(player);
        assignActionsTo(player);

        sendPossibleActions();

        ActionPerformedEvent playerAction = readActionFrom(player);
        if (playerAction != null) {
            player.addAction(playerAction.getAction());

            updatePotBy(playerAction.getAction().getValue());

            if (playerAction.getAction() instanceof Fold)
                room.sendBroadcast(new Events(new PlayerFoldedEvent(player.getNickname())));
            else {
                room.sendBroadcast(new Events(new PlayerUpdatedEvent(player.getNickname(), player.getChips()), new PotUpdatedEvent(turnModel.getPot())));
            }
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
        match.getRoom().sendBroadcast(new Events(optionsEvent));
    }

    private ActionPerformedEvent readActionFrom(PlayerModel player) {
        Events actionEvent = match.getRoom().readMessage(player);
        if (actionEvent != null) {
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
