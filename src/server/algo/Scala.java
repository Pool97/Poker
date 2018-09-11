package server.algo;

import server.model.cards.CardModel;
import utils.Utils;

import java.util.ArrayList;

public class Scala extends PokerHand implements Algorithm {

    public Scala() {
        this.point = 5;
    }

    @Override
    public boolean evaluate(ArrayList<CardModel> initialCards, ArrayList<CardModel> finalCards) {
        ArrayList<CardModel> cards = new ArrayList<>(initialCards);

        Utils.sortCards(cards, true);
        Utils.removeDuplicates(cards);
        checkScala(cards, finalCards);

        if (finalCards.size() != 5) {
            finalCards.clear();
            cards = new ArrayList<>(initialCards);

            Utils.sortCards(cards, false);
            Utils.removeDuplicates(cards);
            checkScala(cards, finalCards);

            if (finalCards.size() == 5)
                return true;
        } else
            return true;

        finalCards.clear();
        return false;
    }

    private void checkScala(ArrayList<CardModel> start, ArrayList<CardModel> ay) {
        int counter = 0;
        for (int i = 0; i < start.size(); i++) {
            if (i != start.size() - 1 && counter != 4) {
                if ((start.get(i).getValue().ordinal() - 1) == start.get(i + 1).getValue().ordinal()) {
                    ay.add(start.get(i));
                    counter++;
                    if (counter == 4)
                        ay.add(start.get(i + 1));
                } else {
                    counter = 0;
                    ay.clear();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "SCALA";
    }
}
