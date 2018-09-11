package server.model;

import server.model.cards.CardModel;

import java.util.ArrayList;
import java.util.Arrays;

public class CommunityModel {
    private ArrayList<CardModel> communityCards;

    public CommunityModel() {
        communityCards = new ArrayList<>();
    }

    public void addCard(CardModel card) {
        communityCards.add(card);
    }

    public void addCards(CardModel... cards) {
        communityCards.addAll(Arrays.asList(cards));
    }

    public CardModel getCard(int index) {
        return communityCards.get(index);
    }

    public ArrayList<CardModel> getCommunityCards() {
        return communityCards;
    }
    public void clear() {
        communityCards.clear();
    }
}
