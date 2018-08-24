package server.model;

import server.events.Events;
import server.events.PlayerDisconnectedEvent;
import server.socket.EventDispatcher;
import server.socket.PlayerController;

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
    private PositionsHandler positionsHandler;
    private EventDispatcher eventDispatcher;

    public Room() {
        players = new ArrayList<>();
        eventDispatcher = new EventDispatcher();
    }

    public void addPlayer(PlayerController player) {
        players.add(player);
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

    public ArrayList<PlayerController> getControllers() {
        return players;
    }

    public void movePlayersPosition() {
        players.stream()
                .filter(player -> !player.getPlayerModel().hasLost())
                .forEach(player -> player.getPlayerModel().setPosition(positionsHandler.nextPosition(player.getPlayerModel().getPosition())));
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

    public Events readMessage(PlayerModel playerModel) {
        PlayerController player = players.stream().filter(client -> client.getPlayerModel().equals(playerModel)).findFirst().get();
        try {
            return eventDispatcher.receiveMessageFrom(player);
        } catch (ExecutionException | InterruptedException e) {
            playerModel.setLost(true);
            playerModel.setDisconnected(true);
            sendBroadcast(new Events(new PlayerDisconnectedEvent(playerModel.getNickname())));
        }
        return null;
    }

    public Events readMessage(PlayerController player) {
        try {
            return eventDispatcher.receiveMessageFrom(player);
        } catch (ExecutionException | InterruptedException e) {
            player.getPlayerModel().setLost(true);
            player.getPlayerModel().setDisconnected(true);
            sendBroadcast(new Events(new PlayerDisconnectedEvent(player.getPlayerModel().getNickname())));
        }
        return null;
    }

    public void sendBroadcast(Events message) {
        players.stream().filter(playerController -> !playerController.getPlayerModel().isDisconnected()).forEach(player -> eventDispatcher.sendMessage(player.getTaskForSend(message)));
    }

    public boolean hasWinner() {
        return getPlayers().stream().filter(playerModel -> !playerModel.hasLost()).count() == 1;
    }

    public String getWinner() {
        if (hasWinner())
            return getPlayers().stream().filter(playerModel -> !playerModel.hasLost()).findFirst().get().getNickname();
        else
            return null;
    }
}