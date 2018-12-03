package server.model.cards;

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

    public ArrayList<CardModel> getCardsList(){
        return cardsList;
    }
}
