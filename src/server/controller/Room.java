package server.controller;

import server.events.EventsContainer;
import server.events.PlayerDisconnectedEvent;
import server.model.PlayerModel;
import server.model.Position;
import server.model.PositionsHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Classe che permette di modellizzare il funzionamento di una stanza da gioco.
 * Si occupa della comunicazione con i Players e di effettuare tutte le modifiche necessarie ai Model dei Players.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Room {
    private ArrayList<PlayerController> players;
    private ArrayList<PlayerController> lostPlayers;
    private PositionsHandler positionsHandler;
    private EventDispatcher eventDispatcher;

    public Room() {
        players = new ArrayList<>();
        lostPlayers = new ArrayList<>();
        eventDispatcher = new EventDispatcher();
    }

    public void addPlayer(PlayerController player) {
        players.add(player);
    }

    public void assignInitialState() {
        players.addAll(lostPlayers);
        lostPlayers.clear();
    }

    public void refreshLists() {
        players.stream().filter(playerController -> playerController.getPlayerModel().hasLost()).forEach(player -> lostPlayers.add(player));
        players.removeIf(playerController -> playerController.getPlayerModel().hasLost());
        removeDisconnectedPlayers();
    }

    public int getSize() {
        return players.size();
    }

    public void sortPlayersByPosition() {
        players.sort(Comparator.comparingInt(client -> client.getPlayerModel().getPosition().ordinal()));
    }

    public ArrayList<PlayerModel> getPlayers() {
        return players.stream().map(PlayerController::getPlayerModel).collect(Collectors.toCollection(ArrayList::new));
    }

    public PlayerController getRoomCreator() {
        if (players.stream().anyMatch(playerController -> playerController.getPlayerModel().isCreator()))
            return players.stream().filter(playerController -> playerController.getPlayerModel().isCreator()).findFirst().get();
        return lostPlayers.stream().filter(playerController -> playerController.getPlayerModel().isCreator()).findFirst().get();
    }

    public ArrayList<PlayerController> getControllers() {
        return players;
    }

    public void movePlayersPosition() {
        players.forEach(player -> player.getPlayerModel().setPosition(positionsHandler.nextPosition(player.getPlayerModel().getPosition())));
    }

    public void removeDisconnectedPlayers() {
        players.removeIf(player -> player.getPlayerModel().isDisconnected());
    }

    public PlayerModel getPlayer(Position position) {
        return getPlayers().stream().filter(player -> player.getPosition() == position).findFirst().get();
    }

    public void setPlayersChips(int chips) {
        players.forEach(client -> client.getPlayerModel().setChips(chips));
    }

    public void assignInitialPositions() {
        positionsHandler = PositionsHandler.createPositions(getSize());
        for (int i = 0; i < getSize(); i++)
            players.get(i).getPlayerModel().setPosition(positionsHandler.getPositions().get(i));
    }

    public void removePosition() {
        positionsHandler.removePosition();
    }

    public Position getNextPosition(Position oldPosition) {
        return positionsHandler.nextPosition(oldPosition);
    }

    public EventsContainer readMessage(PlayerModel playerModel) {
        PlayerController player = players.stream().filter(client -> client.getPlayerModel().equals(playerModel)).findFirst().get();
        try {
            return eventDispatcher.receiveMessageFrom(player);
        } catch (ExecutionException | InterruptedException e) {
            playerModel.setLost(true);
            playerModel.setDisconnected(true);
            refreshLists();
            sendBroadcast(new EventsContainer(new PlayerDisconnectedEvent(playerModel.getNickname())));
        }
        return EventsContainer.createEmptyContainer();
    }

    public EventsContainer readMessage(PlayerController player) {
        try {
            return eventDispatcher.receiveMessageFrom(player);
        } catch (ExecutionException | InterruptedException e) {
            player.getPlayerModel().setLost(true);
            player.getPlayerModel().setDisconnected(true);
            refreshLists();
            sendBroadcast(new EventsContainer(new PlayerDisconnectedEvent(player.getPlayerModel().getNickname())));
        }
        return EventsContainer.createEmptyContainer();
    }

    public void sendBroadcast(EventsContainer message) {
        players.stream().filter(playerController -> !playerController.getPlayerModel().isDisconnected()).forEach(player -> eventDispatcher.sendMessage(player.getTaskForSend(message)));
    }

    public void sendBroadcastToLostPlayers(EventsContainer message) {
        lostPlayers.stream().filter(playerController -> !playerController.getPlayerModel().isDisconnected()).forEach(player -> eventDispatcher.sendMessage(player.getTaskForSend(message)));

    }

    public void sendTo(EventsContainer message, PlayerController player) {
        eventDispatcher.sendMessage(player.getTaskForSend(message));
    }

    public boolean hasWinner() {
        return getPlayers().size() == 1;
    }

    public String getWinner() {
        return getPlayers().stream().findFirst().get().getNickname();
    }
}