package server.algo;

import server.model.cards.CardModel;

import java.util.ArrayList;

public interface Algorithm {
    boolean evaluate(ArrayList<CardModel> initialCards, ArrayList<CardModel> finalCards);
}
