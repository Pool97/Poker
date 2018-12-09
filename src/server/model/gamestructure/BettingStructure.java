package server.model.gamestructure;

import interfaces.Reachable;

public abstract class BettingStructure implements Reachable {
    protected int smallBlind;
    protected int bigBlind;

    public BettingStructure(int smallBlind, int bigBlind){
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
    }
    public BettingStructure() {
        smallBlind = 0;
        bigBlind = 0;
    }

    public abstract void increaseBlinds();

    public void setInitialBlinds() {
        bigBlind = 800;
        smallBlind = bigBlind / 2;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }
}
