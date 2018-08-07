package server.algo;

import server.model.CardModel;

import java.util.ArrayList;

public class Full extends PokerHand implements Algorithm {

    public Full() {
        this.point = 7;
    }

    @Override
    public boolean evaluate(ArrayList<CardModel> initialCards, ArrayList<CardModel> finalCards) {
        if (new PokerOrTris(3).evaluate(initialCards, finalCards)) {
            ArrayList<CardModel> withoutTris = new ArrayList<>(initialCards);
            withoutTris.removeAll(finalCards);
            if (new DoppiaCoppia().evaluate(withoutTris, finalCards) || new Coppia().evaluate(withoutTris, finalCards))
                return true;
        }

        finalCards.clear();
        return false;
    }

    @Override
    public String toString() {
        return "FULL";
    }
}
