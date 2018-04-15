package server;

/**
 * Model della carta da Poker.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 */

public class CardModel {
    private CardRank cardRank;
    private CardSuit cardSuit;

    public CardModel(CardRank cardRank, CardSuit cardSuit) {
        this.cardRank = cardRank;
        this.cardSuit = cardSuit;
    }

    public CardRank getCardRank() {
        return cardRank;
    }

    public void setCardRank(CardRank cardRank) {
        this.cardRank = cardRank;
    }

    public CardSuit getCardSuit() {
        return cardSuit;
    }

    public void setCardSuit(CardSuit cardSuit) {
        this.cardSuit = cardSuit;
    }
}
