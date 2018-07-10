package server.model;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static server.model.Algos.*;

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
                .filter(playerModel -> !playerModel.hasFolded())
                .collect(Collectors.toCollection(ArrayList::new));

        for (PlayerModel player : inGamePlayers) {
            Algos algos = Algos.newInstance(player.getCards(), communityCards.getCard(0), communityCards.getCard(1), communityCards.getCard(2), communityCards.getCard(3),
                    communityCards.getCard(4));

            Algos.scalaReale(communityCards.getCard(0), communityCards.getCard(1), communityCards.getCard(2), communityCards.getCard(3),
                    communityCards.getCard(4), player.getCards().get(0), player.getCards().get(1));
            Algos.scalaColore(communityCards.getCard(0), communityCards.getCard(1), communityCards.getCard(2), communityCards.getCard(3),
                    communityCards.getCard(4), player.getCards().get(0), player.getCards().get(1));
            Algos.poker(communityCards.getCard(0), communityCards.getCard(1), communityCards.getCard(2), communityCards.getCard(3),
                    communityCards.getCard(4), player.getCards().get(0), player.getCards().get(1));
            //Algos.full(communityCards.getCard(0), communityCards.getCard(1), communityCards.getCard(2), communityCards.getCard(3),
            //communityCards.getCard(4), player.getCards().get(0), player.getCards().get(1));

            ArrayList<Pair<CardSuit, CardRank>> colore = algos.colore();
            ArrayList<Pair<CardSuit, CardRank>> scala = algos.scala();
            ArrayList<Pair<CardSuit, CardRank>> tris = algos.tris();
            ArrayList<Pair<CardSuit, CardRank>> doppiaCoppia = algos.doppiaCoppia();
            ArrayList<Pair<CardSuit, CardRank>> coppia = algos.coppia();
            Pair<CardSuit, CardRank> cartaPiuAlta = algos.cartaPiuAlta();

            if (priority1 == 1) {
                System.out.println("SCALA REALE");
                playersHand.put(player.getNickname(), 1);
                playersHandByName.put(player.getNickname(), "SCALA REALE");
            } else if (priority2 == 2) {
                System.out.println("SCALA COLORE");
                playersHand.put(player.getNickname(), 2);
                playersHandByName.put(player.getNickname(), "SCALA COLORE");
            } else if (priority3 == 3) {
                System.out.println("POKER");
                playersHand.put(player.getNickname(), 3);
                playersHandByName.put(player.getNickname(), "POKER");
            } else if (priority4 == 4) {
                System.out.println("FULL");
                playersHand.put(player.getNickname(), 4);
                playersHandByName.put(player.getNickname(), "FULL");
            } else if (priority5 == 5) {
                System.out.println("COLORE");
                playersHand.put(player.getNickname(), 5);
                playersHandByName.put(player.getNickname(), "COLORE");
            } else if (priority6 == 6) {
                System.out.println("SCALA");
                System.out.println(scala.toString());
                playersHand.put(player.getNickname(), 6);
                playersHandByName.put(player.getNickname(), "SCALA");
            } else if (priority7 == 7) {
                System.out.println("TRIS");
                System.out.println(tris.toString());
                playersHand.put(player.getNickname(), 7);
                playersHandByName.put(player.getNickname(), "TRIS");
            } else if (priority8 == 8) {
                System.out.println("COPPIA 1");
                System.out.println(doppiaCoppia.toString());
                playersHand.put(player.getNickname(), 8);
                playersHandByName.put(player.getNickname(), "DOPPIA COPPIA");
            } else if (priority9 == 9) {
                System.out.println("COPPIA");
                System.out.println(coppia.toString());
                playersHand.put(player.getNickname(), 9);
                playersHandByName.put(player.getNickname(), "COPPIA");
            } else if (priority10 == 10) {
                System.out.println("CARTA PIU ALTA");
                System.out.println(cartaPiuAlta);
                playersHand.put(player.getNickname(), 10);
                playersHandByName.put(player.getNickname(), "CARTA PIU ALTA");
            }
            Algos.riazzera();
        }

        return playersHand.entrySet().stream().min(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();
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

        //Determina vincita player
        for (int i = 0; i <= lastIndex - 1; i++) {
            for (Map.Entry<String, Integer> entry : pots.get(lastIndex - 1).entrySet()) {
                winnerSum += entry.getValue();
            }
        }

        //fornisci vincita al player
        players.stream().filter(player -> player.getNickname().equals(winner)).findFirst().get().addChips(winnerSum);

        //restituisci parte del pot
        for (int i = lastIndex; i < pots.size(); i++) {
            HashMap<String, Integer> pot = pots.get(i);
            for (Map.Entry<String, Integer> entry : pot.entrySet()) {
                players.stream().filter(player -> player.getNickname().equals(entry.getKey())).findFirst().get().addChips(entry.getValue());
            }
        }
    }

    public HashMap<String, String> getPlayersHandByName() {
        return playersHandByName;
    }
}
