package server.algorithm;

import server.model.cards.CardModel;

import java.util.ArrayList;

public class ScalaColore extends PokerHand implements Algorithm {

    public ScalaColore() {
        this.point = 9;
    }

    @Override
    public boolean evaluate(ArrayList<CardModel> initialCards, ArrayList<CardModel> finalCards) {
        boolean colore = new Colore().evaluate(initialCards, finalCards);
        finalCards.clear();
        return (colore && new Scala().evaluate(initialCards, finalCards));
    }

    @Override
    public String toString() {
        return "SCALA COLORE";
    }
}
