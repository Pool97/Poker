package server.model;

import javafx.util.Pair;

/**
 * Il Model fondamentale del gioco.
 * Permette di tenere traccia di tutti i cambiamenti avvenuti nel MatchHandler, dal suo inizio alla sua fine.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class TurnModel {
    private int pot;
    private DeckModel deckModel;
    private CommunityModel communityModel;

    /**
     * Costruttore vuoto della classe TurnModel.
     */

    public TurnModel() {
        pot = 0;
        deckModel = new DeckModel();
        deckModel.createAndShuffle();
        communityModel = new CommunityModel();

    }

    public int getPot() {
        return pot;
    }

    /**
     * Permette di resettare a zero il pot.
     * Solitamente questa azione avviene alla fine di ogni turno, quando il pot finale viene riscosso dal PlayerBoard che si è aggiudicato il turno.
     */

    public void resetPot() {
        pot = 0;
    }

    /**
     * Permette di incrementare il pot di una quantità fornita in ingresso al metodo.
     *
     * @param quantity Valore da aggiungere al pot
     */
    public int increasePot(int quantity) {
        return pot += quantity;
    }


    public void createDeck() {
        deckModel.createAndShuffle();
    }

    public Pair<CardSuit, CardRank> getNextCard() {
        return deckModel.nextCard();
    }

    public void addCommunityCards(Pair<CardSuit, CardRank>... cards) {
        communityModel.addCards(cards);
    }
}
