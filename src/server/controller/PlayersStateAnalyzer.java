package server.controller;

import server.model.PlayerModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlayersStateAnalyzer {
    private ArrayList<PlayerModel> players;

    public PlayersStateAnalyzer(ArrayList<PlayerModel> players) {
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
        return players;
    }

    public ArrayList<PlayerModel> getPlayersAtStake() {
        return getPlayers()
                .stream()
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
