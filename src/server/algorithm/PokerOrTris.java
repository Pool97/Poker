package server.algorithm;

import server.model.cards.CardModel;
import utils.Utils;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class PokerOrTris extends PokerHand implements Algorithm {
    private int isPoker;

    public PokerOrTris(int isPoker) {
        this.isPoker = isPoker;
        this.point = isPoker == 4 ? 8 : 4;
    }

    @Override
    public boolean evaluate(ArrayList<CardModel> initialCards, ArrayList<CardModel> finalCards) {
        int num_tris = 0;
        ArrayList<CardModel> finalCards1 = finalCards;
        int initialSize = finalCards1.size();
        for (int i = 0; i < initialCards.size(); i++) {
            if (finalCards1.size() == num_tris * isPoker) {
                for (int j = 0; j < initialCards.size(); j++) {
                    if (i != j) {
                        if (finalCards1.size() == num_tris * isPoker) {
                            if (initialCards.get(i).getValue() == initialCards.get(j).getValue()) {
                                if (!finalCards1.contains(initialCards.get(i)) && !finalCards1.contains(initialCards.get(j))) {
                                    finalCards1.add(initialCards.get(i));
                                    finalCards1.add(initialCards.get(j));
                                }
                            }
                        } else {
                            if (initialCards.get(i).getValue() == initialCards.get(j).getValue()) {
                                finalCards1.add(initialCards.get(j));
                                num_tris++;
                                initialSize = finalCards1.size();
                            }
                        }
                    }
                }
            }
            int num_element_to_remove = finalCards.size() - initialSize;
            IntStream.range(0, num_element_to_remove).forEach(num -> finalCards1.remove(finalCards1.size() - 1));
        }

        if (finalCards1.size() >= isPoker) {
            Utils.sortCards(finalCards1, true);
            return true;
        }
        finalCards1.clear();
        return false;
    }

    @Override
    public String toString() {
        return isPoker == 4 ? "POKER" : "TRIS";
    }
}
