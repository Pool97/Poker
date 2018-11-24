package server.model.automa;

public abstract class Street extends AbstractPokerState{
    public Street() {

    }

    protected void showNextCard() {
        dealer.burnCard();

        dealer.dealCommunityCard();


    }
}
