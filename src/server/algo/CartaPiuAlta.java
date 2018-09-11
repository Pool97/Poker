package server.algo;

import server.model.cards.CardModel;
import utils.Utils;

import java.util.ArrayList;

public class CartaPiuAlta extends PokerHand implements Algorithm {

    public CartaPiuAlta() {
        this.point = 1;
    }

    @Override
    public boolean evaluate(ArrayList<CardModel> initialCards, ArrayList<CardModel> finalCards) {
        ArrayList<CardModel> orderedCard = new ArrayList<>(initialCards);
        Utils.sortCards(orderedCard, true);
        finalCards.add(orderedCard.get(0));
        return true;
    }

    @Override
    public String toString() {
        return "CARTA PIU ALTA";
    }
}
