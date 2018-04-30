package server.automa;

import events.*;
import interfaces.PokerState;
import javafx.util.Pair;
import server.model.*;
import server.socket.ServerManager;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class Action implements PokerState {
    private final static String START_ACTIONS = "È iniziato il giro di puntate non obbligatorie";
    private final static String ONE_PLAYER_ONLY = "È rimasto solo un giocatore! \n";
    private final static String EQUITY_REACHED = "La puntata è stata pareggiata! \n";
    private static int PHASE = 0;
    private TurnModel turnModel;
    private Match match;
    private Events actionEvents;

    public Action(Match match) {
        this.match = match;
        turnModel = match.getTurnModel();
        actionEvents = new Events();
    }


    @Override
    public void goNext() {
        ServerManager serverManager = match.getServerManager();
        serverManager.logger.info(START_ACTIONS + (PHASE + 1) + "\n");
        Room room = serverManager.getRoom();

        PositionManager manager = room.getAvailablePositions();
        Position nextPosition = manager.nextPosition(Position.BB);

        while (!((nextPosition == Position.SB) && (onePlayerRemained() || (isEquityReached())))) {
            PlayerModel player = room.getPlayerByPosition(nextPosition);
            if (!turnModel.hasPlayerFolded(player) && !turnModel.hasPlayerAllIn(player)) {
                doAction(player);
            }
            nextPosition = manager.nextPosition(nextPosition);
        }

        if (onePlayerRemained()) {
            serverManager.logger.info(ONE_PLAYER_ONLY);
            match.setState(new TurnEnd());
        } else if (isEquityReached()) {
            serverManager.logger.info(EQUITY_REACHED);
            if (PHASE == 3) {
                match.setState(new Showdown());
                PHASE++;
            } else if (PHASE == 4) {
                match.setState(new TurnEnd());
                PHASE = 0;
            } else {
                match.setState(new Flop());
                PHASE++;
            }
        }
    }

    public boolean isEquityReached() {
        Room room = match.getServerManager().getRoom();

        ArrayList<PlayerModel> playerNotFolded = room.getPlayers()
                .stream()
                .filter(player -> !turnModel.hasPlayerFolded(player))
                .collect(Collectors.toCollection(ArrayList::new));

        int betMaxAllIn = playerNotFolded.stream()
                .filter(player -> turnModel.hasPlayerAllIn(player))
                .mapToInt(player -> turnModel.getTurnBet(player))
                .max()
                .orElse(0);

        ArrayList<PlayerModel> playerNotAllIn = playerNotFolded
                .stream()
                .filter(player -> !turnModel.hasPlayerAllIn(player))
                .collect(Collectors.toCollection(ArrayList::new));

        if (playerNotAllIn
                .stream()
                .anyMatch(player -> turnModel.getTurnBet(player) < betMaxAllIn))
            return false;

        long distinctBets = playerNotAllIn
                .stream()
                .mapToInt(player -> turnModel.getTurnBet(player))
                .distinct()
                .count();

        return distinctBets <= 1;
    }

    public boolean onePlayerRemained() {
        long notFold = match.getServerManager().getRoom().getPlayers()
                .stream()
                .filter(player -> !turnModel.hasPlayerFolded(player))
                .count();
        return notFold == 1;
    }

    /**
     * Player che deve puntare attualmente
     *
     * @param player
     * @return
     */

    public void doAction(PlayerModel player) {
        ServerManager manager = match.getServerManager();
        Room room = manager.getRoom();

        int maxValue = room.getPlayers()
                .stream()
                .mapToInt(roomPlayer -> turnModel.getTurnBet(roomPlayer))
                .max()
                .orElse(0);

        int callValue = maxValue - turnModel.getTurnBet(player);

        ActionOptionsEvent optionsEvent = new ActionOptionsEvent();
        optionsEvent.addOption(new Pair<>(ActionType.FOLD, 0));

        if (callValue > 0)
            optionsEvent.addOption(new Pair<>(ActionType.CALL, callValue));
        else
            optionsEvent.addOption(new Pair<>(ActionType.CHECK, 0));

        if (player.getTotalChips() > callValue)
            optionsEvent.addOption(new Pair<>(ActionType.RAISE, player.getTotalChips()));

        actionEvents.addEvent(optionsEvent);
        manager.sendMessage(room.getPlayerSocket(player), new CountDownLatch(1), actionEvents); //invia tutte le opzioni che ha il player ha a disposizione
        actionEvents.removeAll();

        actionEvents = manager.listenForAMessage(room.getPlayerSocket(player));
        setActionEvents(player);
        manager.sendMessage(room.getConnections(), new CountDownLatch(1), actionEvents); //invia a tutti la puntata effettuata dal player
        actionEvents.removeAll();
    }

    private void setActionEvents(PlayerModel player) {
        ActionPerformedEvent playerAction = (ActionPerformedEvent) actionEvents.getEvent();
        turnModel.addAction(player, playerAction.getAction());
        actionEvents.addEvent(new PlayerUpdatedEvent(player));
        actionEvents.addEvent(new PotUpdatedEvent(turnModel.increasePot(playerAction.getAction().getValue())));
    }
}
