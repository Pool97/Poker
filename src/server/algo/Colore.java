package server.algo;

import server.model.cards.CardModel;
import utils.Utils;

import java.util.ArrayList;

public class Colore extends PokerHand implements Algorithm {

    public Colore() {
        this.point = 6;
    }

    @Override
    public boolean evaluate(ArrayList<CardModel> initialCards, ArrayList<CardModel> finalCards) {
        for (int i = 0; i < initialCards.size(); i++) {
            finalCards.add(initialCards.get(i));
            for (int j = 0; j < initialCards.size(); j++) {
                if (i != j) {
                    if (initialCards.get(i).getKey() == initialCards.get(j).getKey()) {
                        finalCards.add(initialCards.get(j));
                    }
                }
            }
            if (finalCards.size() < 5) {
                finalCards.clear();
            } else
                break;
        }

        if (finalCards.size() >= 5) {
            Utils.sortCards(finalCards, true);
            return true;
        }

        finalCards.clear();
        return false;
    }

    @Override
    public String toString() {
        return "COLORE";
    }
}
