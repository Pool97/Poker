package server.messages;

import interfaces.Message;

import java.io.Serializable;

public class StartMessage implements Message, Serializable {
    private int bigBlind;
    private int smallBlind;
    private int pot;
    private int startingChipAmount;

    public StartMessage(int bigBlind, int smallBlind, int pot, int startingChipAmount) {
        this.bigBlind = bigBlind;
        this.smallBlind = smallBlind;
        this.pot = pot;
        this.startingChipAmount = startingChipAmount;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getPot() {
        return pot;
    }

    public int getStartingChipAmount() {
        return startingChipAmount;
    }
}
