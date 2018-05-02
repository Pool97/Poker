package server.automa;

import events.*;
import interfaces.PokerState;
import interfaces.TransitionStrategy;
import javafx.util.Pair;
import server.model.*;
import server.socket.ServerManager;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Action implements PokerState {
    private TransitionStrategy strategy;
    private Match match;

    private final static String START_ACTIONS = "È iniziato il giro di puntate non obbligatorie";
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore! \n";
    private final static String EQUITY_REACHED = "La puntata è stata pareggiata! \n";

    public Action(Match match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        ServerManager.logger.info(START_ACTIONS + "\n");
        Room room = match.getRoom();
        Position nextPosition = match.getMatchModel().getNextPosition(Position.BB);

        while (!((nextPosition == Position.SB) && (onePlayerRemained() || (isEquityReached())))) {
            PlayerModel player = room.getPlayer(nextPosition);
            if (!player.hasFolded() && !player.isAllIn()) {
                doAction(player);
            }
            nextPosition = match.getMatchModel().getNextPosition(nextPosition);
        }

        if (onePlayerRemained()) {
            ServerManager.logger.info(ONE_PLAYER_ONLY);
            match.setState(new TurnEnd());
        } else if (isEquityReached()) {
            ServerManager.logger.info(EQUITY_REACHED);
            strategy.makeTransition();
        }
    }

    private boolean isEquityReached() {
        Room room = match.getRoom();

        ArrayList<PlayerModel> playerNotFolded = room.getPlayers()
                .stream()
                .filter(player -> !player.hasFolded())
                .collect(Collectors.toCollection(ArrayList::new));

        int betMaxAllIn = playerNotFolded.stream()
                .filter(PlayerModel::isAllIn)
                .mapToInt(PlayerModel::getTurnBet)
                .max()
                .orElse(0);

        ArrayList<PlayerModel> playerNotAllIn = playerNotFolded
                .stream()
                .filter(player -> !player.isAllIn())
                .collect(Collectors.toCollection(ArrayList::new));

        if (playerNotAllIn
                .stream()
                .anyMatch(player -> player.getTurnBet() < betMaxAllIn))
            return false;

        long distinctBets = playerNotAllIn
                .stream()
                .mapToInt(PlayerModel::getTurnBet)
                .distinct()
                .count();

        return distinctBets <= 1;
    }

    private boolean onePlayerRemained() {
        long notFold = match.getRoom().getPlayers()
                .stream()
                .filter(player -> !player.hasFolded())
                .count();
        return notFold == 1;
    }

    public void setTransitionStrategy(TransitionStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Player che deve puntare attualmente
     *
     * @param player
     * @return
     */

    private void doAction(PlayerModel player) {
        Room room = match.getRoom();
        TurnModel turnModel = match.getTurnModel();

        int maxValue = room.getPlayers()
                .stream()
                .mapToInt(PlayerModel::getTurnBet)
                .max()
                .orElse(0);

        int callValue = maxValue - player.getTurnBet();

        ActionOptionsEvent optionsEvent = new ActionOptionsEvent();
        optionsEvent.addOption(new Pair<>(ActionType.FOLD, 0));

        if (callValue > 0)
            optionsEvent.addOption(new Pair<>(ActionType.CALL, callValue));
        else
            optionsEvent.addOption(new Pair<>(ActionType.CHECK, 0));

        if (player.getChips() > callValue)
            optionsEvent.addOption(new Pair<>(ActionType.RAISE, player.getChips()));

        room.sendMessage(player, new Events(optionsEvent)); //invia tutte le opzioni che ha il player ha a disposizione

        Events actionPerformed = room.readMessage(player);
        ActionPerformedEvent playerAction = (ActionPerformedEvent) actionPerformed.getEvent();
        player.addAction(playerAction.getAction());
        turnModel.increasePot(playerAction.getAction().getValue());

        room.sendBroadcast(new Events(new PlayerUpdatedEvent(player), new PotUpdatedEvent(turnModel.getPot()))); //invia a tutti la puntata effettuata dal player
    }
}
