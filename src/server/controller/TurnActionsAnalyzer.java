package server.controller;

import server.model.PlayerModel;

import java.util.ArrayList;

public class TurnActionsAnalyzer {

    public TurnActionsAnalyzer() {

    }

    public int maxBetAmong(ArrayList<PlayerModel> players) {
        return players.stream().mapToInt(PlayerModel::getTurnBet)
                .max()
                .orElse(0);
    }

    public int countDistinctBets(ArrayList<PlayerModel> players) {
        return (int) players
                .stream()
                .mapToInt(PlayerModel::getTurnBet)
                .distinct()
                .count();
    }

    public boolean hasBetHappenedYet(ArrayList<PlayerModel> players) {
        return players.stream().anyMatch(PlayerModel::hasBetted);
    }
}
