package server.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class CommunityModel {
    private ArrayList<Pair<CardSuit, CardRank>> communityCards;

    public CommunityModel() {
        communityCards = new ArrayList<>();
    }

    public void addCard(Pair<CardSuit, CardRank> card) {
        communityCards.add(card);
    }

    public void addCards(Pair<CardSuit, CardRank>... cards) {
        communityCards.addAll(Arrays.asList(cards));
    }

    public Pair<CardSuit, CardRank> getCard(int index) {
        return communityCards.get(index);
    }
}
