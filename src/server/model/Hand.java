package server.model;

import server.model.cards.CardModel;

import java.util.ArrayList;

public class Hand {
    private ArrayList<CardModel> cards;

    public Hand(){
        cards = new ArrayList<>();
    }

    public void addCards(CardModel first, CardModel second){
        cards.add(first);
        cards.add(second);
    }
    public void giveBackCards(){
        cards.clear();
    }

    public ArrayList<CardModel> getHand(){
        return cards;
    }
}
