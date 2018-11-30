package server.model;

import server.algorithm.PokerHandsEvaluator;
import server.model.CommunityModel;
import server.model.PlayerModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TurnWinnerEvaluator {
    private ArrayList<PlayerModel> players;
    private CommunityModel communityCards;
    private HashMap<String, Integer> playersHand;
    private HashMap<String, String> playersHandByName;

    public TurnWinnerEvaluator(ArrayList<PlayerModel> players, CommunityModel communityCards) {
        playersHand = new HashMap<>();
        playersHandByName = new HashMap<>();
        this.communityCards = communityCards;
        this.players = players;

    }

    public String evaluateTurnWinner() {
        ArrayList<PlayerModel> inGamePlayers = players.stream()
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

    public String getPlayerHandByName(String nickname) {
        return playersHandByName.get(nickname);
    }
}
