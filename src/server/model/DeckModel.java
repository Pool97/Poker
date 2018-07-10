package server.model;


import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Classe che è incaricata di gestire la logica del deck di Poker. Tra i possibili metodi possiamo trovare
 * la creazione del Deck, la distribuzione delle carte.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 */

public class DeckModel {
    private Stack<CardModel> deck;

    private static List<Pair<Integer, Integer>> createDeck(){
        List<Pair<Integer, Integer>> orderedDeck = new ArrayList<>();
        for (int i = 0; i < 13; ++i)
            for (int j = 0; j < 4; ++j)
                orderedDeck.add(new Pair<>(j, i));
        return orderedDeck;
    }

    /**
     * Permette la creazione di un Deck di carte da Poker e il suo mescolamento.
     */

    public void createAndShuffle(){
        deck = new Stack<>();
        List<Pair<Integer, Integer>> deckShuffled = createDeck();
        Collections.shuffle(deckShuffled);
        deck = deckShuffled.stream()
                .map(pair -> new CardModel(CardSuit.values()[pair.getKey()], CardRank.values()[pair.getValue()]))
                .collect(Collectors.toCollection(Stack<CardModel>::new));
    }

    public CardModel nextCard() {
        return deck.pop();
    }
}
