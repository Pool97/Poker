package server.model;

import server.model.cards.CardModel;

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

    public TurnModel() {
        pot = 0;
        deckModel = new DeckModel();
        deckModel.shuffle();
        communityModel = new CommunityModel();

    }

    public int getPot() {
        return pot;
    }

    public void resetPot() {
        pot = 0;
    }

    public int increasePot(int quantity) {
        return pot += quantity;
    }

    public void createDeck() {
        deckModel.shuffle();
    }

    public CardModel getNextCard() {
        return deckModel.nextCard();
    }

    public CommunityModel getCommunityModel() {
        return communityModel;
    }

    public void addCommunityCards(CardModel... cards) {
        communityModel.addCards(cards);
    }

    public void emptyCommunity() {
        communityModel.clear();
    }
}
