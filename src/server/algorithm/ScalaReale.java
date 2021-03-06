package server.algorithm;

import server.model.cards.CardModel;
import server.model.cards.CardRank;
import server.model.cards.CardSuit;

import java.util.ArrayList;

public class ScalaReale extends PokerHand implements Algorithm {

    public ScalaReale() {
        this.point = 10;
    }

    @Override
    public boolean evaluate(ArrayList<CardModel> initialCards, ArrayList<CardModel> finalCards) {
        boolean isScalaReale = false;

        if (new ScalaColore().evaluate(initialCards, finalCards))
            isScalaReale = finalCards.get(0).getKey() == CardSuit.HEARTS && finalCards.get(0).getValue() == CardRank.ACE;

        if (!isScalaReale) {
            finalCards.clear();
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "SCALA REALE";
    }
}
