package server.model.actions;

import server.events.PlayerTurnEvent;

public class ActionBuilderFacade {
    private PossibleActionsBuilder builder;

    public ActionBuilderFacade(int playerChips, int callValue, int bigBlind) {
        builder = new PossibleActionsBuilder(playerChips, callValue, bigBlind);
    }

    public PlayerTurnEvent buildActions(boolean isBetHappened) {
        builder.appendFold();
        builder.appendCall();
        builder.appendCheck();
        builder.appendBetOrRaise(isBetHappened);
        return builder.getPossibleActions();
    }
}
