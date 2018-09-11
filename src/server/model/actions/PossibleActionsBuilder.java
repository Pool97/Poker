package server.model.actions;

import server.events.PlayerTurnEvent;

public class PossibleActionsBuilder {
    private PlayerTurnEvent options;
    private int playerChips;
    private int callValue;
    private int bigBlind;

    public PossibleActionsBuilder(int playerChips, int callValue, int bigBlind) {
        options = new PlayerTurnEvent();
        this.playerChips = playerChips;
        this.callValue = callValue;
        this.bigBlind = bigBlind;
    }

    public void appendFold() {
        options.addOption(new Fold());
    }

    public void appendCall() {
        if (callValue > 0) {
            options.addOption(getDelta() < 0 ? new Call(playerChips) : new Call(callValue));
        }
    }

    public void appendCheck() {
        if (callValue == 0) {
            options.addOption(new Check());
        }
    }

    public void appendBetOrRaise(boolean isBetHappened) {
        if (getDelta() > 0)
            options.addOption(isBetHappened ? new RaiseOption(callValue + 1, playerChips) : new BetOption(bigBlind * 2, playerChips));
    }

    private int getDelta() {
        return playerChips - callValue;
    }

    public PlayerTurnEvent getPossibleActions() {
        return options;
    }
}
