package server.model;


import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe che è incaricata di gestire la logica del deck di Poker. Tra i possibili metodi possiamo trovare
 * la creazione del Deck, la distribuzione delle carte.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 */

public class DeckModel {

    private Set<Pair<CardSuit, CardRank>> deck;

    /**
     * Costruttore vuoto di DeckModel
     */

    public DeckModel(){
        deck = new HashSet<>();
    }

    /**
     * Permette di creare il Deck, effettuando un mescolamento di tutte le carte.
     * Ancora da testare il suo funzionamento.
     */

    public void shuffle(){
        List<Pair<Integer, Integer>> shuffled = new ArrayList<>();
        for (int i = 0; i < 14; ++i)
            for (int j = 0; j < 4; ++j)
                shuffled.add(new Pair<>(i, j));

       Collections.shuffle(shuffled);
       deck = shuffled.stream().map(pair -> new Pair<>(CardSuit.values()[pair.getKey()], CardRank.values()[pair.getValue()])).collect(Collectors.toSet());
    }
}
