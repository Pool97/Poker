package server.model;

import server.model.cards.CardModel;

import java.util.ArrayList;

public class CommunityModel {
    private ArrayList<CardModel> cardsList;

    public CommunityModel() {
        cardsList = new ArrayList<>();
    }

    public void addCard(CardModel card) {
        cardsList.add(card);
    }

    public CardModel getCard(int index) {
        return cardsList.get(index);
    }

    public void clear() {
        cardsList.clear();
    }
}
