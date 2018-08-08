package server.automa;

import client.events.ActionPerformedEvent;
import interfaces.PokerState;
import server.events.Events;
import server.events.PlayerTurnEvent;
import server.events.PlayerUpdatedEvent;
import server.events.PotUpdatedEvent;
import server.model.*;

public abstract class Action implements PokerState {
    private final static String ACTION_PERFORMED = "Puntata effettuata da: ";
    private final static String PLAYER_CHIPS = "Le Chips attuali di: ";
    private final static String PLAYER_OPTIONS = "Propongo al player le opzioni disponibili per la puntata... \n";
    private final static String PLAYERS_INFO = "Informo tutti i players della puntata effettuata... \n";
    protected MatchHandler match;
    private PlayerTurnEvent optionsEvent;

    public Action(MatchHandler match) {
        this.match = match;
    }


    protected boolean isEquityReached() {
        if (match.getRoom().checkIfAllInActionsAreEqualized())
            return false;

        return match.getRoom().countDistinctBets() <= 1;
    }


    protected boolean checkIfOnePlayerRemained() {
        return match.getRoom().getPlayers()
                .stream()
                .filter(player -> !player.hasFolded())
                .count() == 1;
    }

    protected void doAction(PlayerModel player) {
        Room room = match.getRoom();
        TurnModel turnModel = match.getTurnModel();

        int maxValue = room.calculateMaxTurnBet();

        int callValue = maxValue - player.getTurnBet();

        optionsEvent = new PlayerTurnEvent(player.getNickname());
        optionsEvent.addOption(new Fold());

        if (callValue > 0) {
            if (player.getChips() - callValue < 0)
                optionsEvent.addOption(new Call(player.getChips()));
            else {
                optionsEvent.addOption(new Call(callValue));
            }
        }
        else
            optionsEvent.addOption(new Check());

        if (player.getChips() > callValue)
            optionsEvent.addOption(new RaiseOption(callValue + 1, player.getChips()));

        MatchHandler.logger.info(PLAYER_OPTIONS);

        sendPossibleActionsTo(player);

        ActionPerformedEvent playerAction = readActionFrom(player);
        MatchHandler.logger.info(ACTION_PERFORMED + player.getNickname() + " " + playerAction.getAction().getValue());
        player.addAction(playerAction.getAction());
        MatchHandler.logger.info(PLAYER_CHIPS + player.getNickname() + ": " + player.getChips());

        turnModel.increasePot(playerAction.getAction().getValue());

        MatchHandler.logger.info(PLAYERS_INFO);
        room.sendBroadcast(new Events(new PlayerUpdatedEvent(player.getNickname(), player.getChips()), new PotUpdatedEvent(turnModel.getPot())));
    }

    private void sendPossibleActionsTo(PlayerModel player) {
        match.getRoom().sendBroadcast(new Events(optionsEvent));
    }

    private ActionPerformedEvent readActionFrom(PlayerModel player) {
        Events actionEvent = match.getRoom().readMessage(player);
        return (ActionPerformedEvent) actionEvent.getEvent();
    }
}
