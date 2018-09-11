package server.controller;

import server.model.PlayerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PotHandler {
    private ArrayList<PlayerModel> players;
    private HashMap<String, Integer> playersStack;
    private ArrayList<HashMap<String, Integer>> pots;

    public PotHandler(ArrayList<PlayerModel> players) {
        this.players = players;
        playersStack = new HashMap<>();
        pots = new ArrayList<>();
        storeStacksInMap();
        createPots();
    }

    private void createPots() {
        while (!playersStack.isEmpty()) {
            pots.add(new HashMap<>());
            int sidePotValue = findSmallestStackValue();
            loadSidePotByValue(sidePotValue);
            removeEmptyStacks();
        }
    }

    private void storeStacksInMap() {
        players.forEach(player -> playersStack.put(player.getNickname(), player.getTurnBetWithoutDeadMoney()));
    }

    private int findSmallestStackValue() {
        return playersStack.values()
                .stream()
                .min(Integer::compare)
                .orElse(0);
    }

    private void loadPlayerStackInPot(String nickname, int potValue) {
        pots.get(pots.size() - 1).put(nickname, potValue);
    }

    private void decreasePlayerStackByPotValue(String nickname, int potValue) {
        playersStack.put(nickname, playersStack.get(nickname) - potValue);
    }

    private void removeEmptyStacks() {
        playersStack.entrySet().removeIf(entry -> entry.getValue() == 0);
    }

    private void loadSidePotByValue(int value) {
        playersStack.keySet().forEach(nickname -> {
            loadPlayerStackInPot(nickname, value);
            decreasePlayerStackByPotValue(nickname, value);
        });
    }

    public void assignPotsTo(String winner) {
        assignWinningAmountTo(winner);
        giveBackChipsInExcessToLosers(getLastPositionInPotsOf(winner));
    }

    private void assignWinningAmountTo(String winner) {
        players.stream()
                .filter(player -> player.getNickname().equals(winner))
                .findFirst()
                .get()
                .addChips(getWinningAmountOf(winner));
    }

    private int getWinningAmountOf(String player) {
        int winningAmount = 0;
        for (int i = 0; i <= getLastPositionInPotsOf(player) - 1; i++)
            for (Map.Entry<String, Integer> entry : pots.get(i).entrySet())
                winningAmount += entry.getValue();
        return winningAmount;
    }

    private int getLastPositionInPotsOf(String player) {
        return (int) pots.stream().filter(pot -> isPlayerInPot(pot, player)).count();
    }

    private boolean isPlayerInPot(HashMap<String, Integer> pot, String player) {
        return pot.entrySet().stream().anyMatch(entry -> entry.getKey().equals(player));
    }

    private void giveBackChipsInExcessTo(String nickname, int chips) {
        players.stream().filter(player -> player.getNickname().equals(nickname)).findFirst().get().addChips(chips);
    }

    private void giveBackChipsInExcessToLosers(int startPot) {
        for (int i = startPot; i < pots.size(); i++) {
            HashMap<String, Integer> pot = pots.get(i);
            for (Map.Entry<String, Integer> entry : pot.entrySet())
                giveBackChipsInExcessTo(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Pots: \n");

        for (HashMap<String, Integer> pot : pots) {
            builder.append("POT: \n");
            builder.append(pot.keySet().toString()).append("\n");
            builder.append(pot.values().toString());
        }

        return builder.toString();
    }
}
