package server.model;

import server.socket.PlayerController;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlayersStateAnalyzer {
    private ArrayList<PlayerController> players;

    public PlayersStateAnalyzer(ArrayList<PlayerController> players) {
        this.players = players;
    }

    public int countPlayersAtStake() {
        return getPlayersAtStake().size();
    }

    public int countAllInPlayers() {
        return getAllInPlayers().size();
    }

    public int countActivePlayers() {
        return getActivePlayers().size();
    }

    public boolean isAllPlayersAtStakeAllIn() {
        return countActivePlayers() == 0 && countAllInPlayers() >= 1;
    }


    public ArrayList<PlayerModel> getPlayers() {
        return players.stream().map(PlayerController::getPlayerModel).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<PlayerModel> getPlayersAtStake() {
        return getPlayers()
                .stream()
                .filter(playerModel -> !playerModel.hasLost())
                .filter(playerModel -> !playerModel.hasFolded())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<PlayerModel> getAllInPlayers() {
        return getPlayersAtStake().stream()
                .filter(PlayerModel::isAllIn)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<PlayerModel> getActivePlayers() {
        return getPlayersAtStake().stream()
                .filter(playerModel -> !playerModel.isAllIn())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
