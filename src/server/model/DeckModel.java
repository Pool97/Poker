package server.model;


import javafx.util.Pair;
import server.model.cards.CardModel;
import server.model.cards.CardRank;
import server.model.cards.CardSuit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DeckModel {
    private Stack<CardModel> deck;

    private static List<Pair<Integer, Integer>> createDeck(){
        List<Pair<Integer, Integer>> orderedDeck = new ArrayList<>();
        for (int i = 0; i < 13; ++i)
            for (int j = 0; j < 4; ++j)
                orderedDeck.add(new Pair<>(j, i));
        return orderedDeck;
    }

    public void shuffle() {
        deck = new Stack<>();
        List<Pair<Integer, Integer>> deckShuffled = createDeck();
        Collections.shuffle(deckShuffled);
        for (Pair<Integer, Integer> pair : deckShuffled) {
            deck.push(new CardModel(CardSuit.values()[pair.getKey()], CardRank.values()[pair.getValue()]));
        }
    }

    public CardModel nextCard() {
        return deck.pop();
    }
}
