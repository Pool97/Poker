package server.algo;

import server.model.CardModel;
import utils.Utils;

import java.util.ArrayList;

public class DoppiaCoppia extends PokerHand implements Algorithm {

    public DoppiaCoppia() {
        this.point = 3;
    }

    @Override
    public boolean evaluate(ArrayList<CardModel> initialCards, ArrayList<CardModel> finalCards) {
        if (Utils.checkNumberOfCouples(initialCards, finalCards) >= 2) {
            Utils.sortCards(finalCards, true);
            return true;
        }
        finalCards.clear();
        return false;
    }

    @Override
    public String toString() {
        return "DOPPIA COPPIA";
    }
}
