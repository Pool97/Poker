package server.algo;

import server.model.cards.CardModel;
import utils.Utils;

import java.util.ArrayList;

public class Coppia extends PokerHand implements Algorithm {

    public Coppia() {
        this.point = 2;
    }

    @Override
    public boolean evaluate(ArrayList<CardModel> initialCards, ArrayList<CardModel> finalCards) {
        if (Utils.checkNumberOfCouples(initialCards, finalCards) == 1) {
            Utils.sortCards(finalCards, true);
            return true;
        }

        finalCards.clear();
        return false;
    }

    @Override
    public String toString() {
        return "COPPIA";
    }
}
