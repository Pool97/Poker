package server.model;

public class MatchModel {
    private int smallBlind;
    private int bigBlind;
    private int startChips;

    public MatchModel(int smallBlind, int bigBlind, int startChips) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.startChips = startChips;
    }

    public MatchModel() {
        smallBlind = 0;
        bigBlind = 0;
    }

    public void increaseBlinds() {
        if (bigBlind < startChips) {
            bigBlind *= 2;
            smallBlind = bigBlind / 2;
        }
    }

    public void setInitialBlinds() {
        bigBlind = startChips / 50;
        smallBlind = bigBlind / 2;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getStartChips() {
        return startChips;
    }

    public void setStartChips(int startChips) {
        this.startChips = startChips;
    }



}
