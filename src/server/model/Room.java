package server.model;

import interfaces.Message;
import server.socket.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private ArrayList<Player> players;
    private PositionManager positionManager;
    private final ExecutorService poolExecutor = Executors.newCachedThreadPool();

    public Room() {
        players = new ArrayList<>();
        positionManager = new PositionManager();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public int getSize() {
        return players.size();
    }

    public void sortByPosition() {
        players.sort(Comparator.comparingInt(client -> client.getPlayerModel().getPosition().ordinal()));
    }

    public ArrayList<PlayerModel> getPlayers() {
        return players.stream().map(Player::getPlayerModel).collect(Collectors.toCollection(ArrayList::new));
    }

    public void movePlayersPosition() {
        players.stream()
                .filter(player -> !player.getPlayerModel().hasLost())
                .forEach(player -> player.getPlayerModel().setPosition(positionManager.nextPosition(player.getPlayerModel().getPosition())));
    }

    public PlayerModel getPlayer(Position position) {
        return getPlayers().stream().filter(player -> player.getPosition() == position).findFirst().get();
    }

    public void setPlayersChips(int chips) {
        players.forEach(client -> client.getPlayerModel().setChips(chips));
    }

    public void setPlayersPositions() {
        for (int i = 0; i < getSize(); i++)
            players.get(i).getPlayerModel().setPosition(positionManager.getPositions().get(i));
    }

    public void setAvailablePositions(int size) {
        positionManager.addPositions(size);
    }

    public void removePosition() {
        positionManager.removePosition();
    }
    public Position getNextPosition(Position oldPosition) {
        return positionManager.nextPosition(oldPosition);
    }

    public <T extends Message> T readMessage(PlayerModel playerModel) {
        T message;
        Player player = players.stream().filter(client -> client.getPlayerModel().equals(playerModel)).findFirst().get();
        message = player.readMessage(poolExecutor);
        return message;
    }

    public <T extends Message> T readMessage(Player player) {
        return player.readMessage(poolExecutor);
    }

    public <T extends Message> void sendMessage(PlayerModel player, T message) {
        Player clientSocket = players.stream().filter(client -> client.getPlayerModel().equals(player)).findFirst().orElse(null);
        clientSocket.sendMessage(poolExecutor, message);
    }

    public <T extends Message> void sendBroadcast(T message) {
        players.forEach(player -> player.sendMessage(poolExecutor, message));
    }

    public boolean hasWinner() {
        return getPlayers().stream().filter(playerModel -> !playerModel.hasLost()).count() == 1;
    }

    public PlayerModel getWinner() {
        if (hasWinner())
            return getPlayers().stream().filter(playerModel -> !playerModel.hasLost()).findFirst().get();
        else
            return null;
    }

    public ArrayList<PlayerModel> getPlayersInGame() {
        return getPlayers()
                .stream()
                .filter(playerModel -> !playerModel.hasLost())
                .filter(player -> !player.hasFolded())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<PlayerModel> getPlayersInGameWithChips() {
        return getPlayersInGame()
                .stream()
                .filter(player -> !player.isAllIn())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int calculateMaxAllInBet() {
        return getPlayersInGame().stream()
                .filter(PlayerModel::isAllIn)
                .mapToInt(PlayerModel::getTurnBet)
                .max()
                .orElse(0);
    }

    public int calculateMaxTurnBet() {
        return getPlayers()
                .stream()
                .mapToInt(PlayerModel::getTurnBet)
                .max()
                .orElse(0);
    }

    public long countDistinctBets() {
        return getPlayersInGameWithChips()
                .stream()
                .mapToInt(PlayerModel::getTurnBet)
                .distinct()
                .count();
    }

    public boolean checkIfAllInActionsAreEqualized() {
        return getPlayersInGameWithChips()
                .stream()
                .anyMatch(player -> player.getTurnBet() < calculateMaxAllInBet());
    }
}