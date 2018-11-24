package server.algorithm;

public abstract class PokerHand implements Algorithm {
    protected int point;

    public PokerHand() {

    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
