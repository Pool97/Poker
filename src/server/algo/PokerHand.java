package server.algo;

public abstract class PokerHand implements Algorithm {
    protected int point;

    public PokerHand() {

    }

    public int getPoint() {
        return point;
    }
}
