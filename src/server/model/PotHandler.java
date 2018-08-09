package server.model;

import server.algo.PokerHandsEvaluator;

import java.util.*;
import java.util.stream.Collectors;

public class PotHandler {
    private ArrayList<PlayerModel> players;
    private CommunityModel communityCards;

    private HashMap<String, Integer> totalChipsForEachPlayer;
    private HashMap<String, Integer> playersHand;
    private HashMap<String, String> playersHandByName;
    private ArrayList<HashMap<String, Integer>> pots;

    public PotHandler(ArrayList<PlayerModel> players, CommunityModel communityCards) {
        this.players = players;
        totalChipsForEachPlayer = new HashMap<>();
        playersHand = new HashMap<>();
        playersHandByName = new HashMap<>();
        this.communityCards = communityCards;
        pots = new ArrayList<>();
        sumAllBetForEachPlayer();
        createPots();
    }

    private void sumAllBetForEachPlayer() {
        players.forEach(player -> totalChipsForEachPlayer.put(player.getNickname(), player.getTurnBet()));
    }

    private void createPots() {
        while (!totalChipsForEachPlayer.isEmpty()) {
            HashMap<String, Integer> sidePot = new HashMap<>();
            int minValue = totalChipsForEachPlayer.values()
                    .stream()
                    .min(Integer::compare)
                    .orElse(0);

            Iterator<String> players = totalChipsForEachPlayer.keySet().iterator();
            while (players.hasNext()) {
                String player = players.next();
                sidePot.put(player, minValue);
                int playerValueRemained = totalChipsForEachPlayer.get(player) - minValue;
                totalChipsForEachPlayer.put(player, playerValueRemained);

            }

            pots.add(sidePot);
            totalChipsForEachPlayer.entrySet().removeIf(entry -> entry.getValue() == 0);
        }
    }

    public String evaluateTurnWinner() {
        ArrayList<PlayerModel> inGamePlayers = players.stream()
                .filter(playerModel -> !playerModel.hasLost())
                .filter(playerModel -> !playerModel.hasFolded())
                .collect(Collectors.toCollection(ArrayList::new));

        for (PlayerModel player : inGamePlayers) {
            PokerHandsEvaluator evaluator = new PokerHandsEvaluator(player.getCards(), communityCards.getCard(0), communityCards.getCard(1), communityCards.getCard(2), communityCards.getCard(3),
                    communityCards.getCard(4));
            evaluator.evaluate();

            playersHand.put(player.getNickname(), evaluator.getPlayerPoint());
            playersHandByName.put(player.getNickname(), evaluator.getPlayerPointName());
        }

        return playersHand.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();
    }

    public String toString() {
        System.out.println("All Pots: ");

        for (HashMap<String, Integer> pot : pots) {
            System.out.println("POT");
            System.out.println(pot.keySet().toString());
            System.out.println(pot.values().toString());
        }
        return "";
    }

    public void assignPots(String winner) {
        int lastIndex = 0;

        //determina posizione più alta del player nel pot
        for (HashMap<String, Integer> pot : pots) {
            if (pot.entrySet().stream().anyMatch(entry -> entry.getKey().equals(winner)))
                lastIndex++;
            else
                break;
        }

        int winnerSum = 0;
        System.out.println("LAstIndex" + lastIndex);
        //Determina vincita player
        for (int i = 0; i <= lastIndex - 1; i++) {
            for (Map.Entry<String, Integer> entry : pots.get(i).entrySet()) {
                winnerSum += entry.getValue();
            }
        }

        System.out.println(winnerSum);
        //fornisci vincita al player
        players.stream().filter(player -> player.getNickname().equals(winner)).findFirst().get().addChips(winnerSum);

        //restituisci parte del pot
        for (int i = lastIndex; i < pots.size(); i++) {
            HashMap<String, Integer> pot = pots.get(i);
            for (Map.Entry<String, Integer> entry : pot.entrySet()) {
                players.stream().filter(playerModel -> playerModel.getNickname().equals(entry.getKey()))
                        .forEach(playerModel -> System.out.println("Il player " + playerModel.getNickname() + "vuole essere risarcito per" + entry.getValue() + "big dlin dlin"));

                players.stream().filter(player -> player.getNickname().equals(entry.getKey())).findFirst().get().addChips(entry.getValue());
            }
        }
    }

    public HashMap<String, String> getPlayersHandByName() {
        return playersHandByName;
    }
}
