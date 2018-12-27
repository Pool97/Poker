package server.model.automa;

import server.controller.Game;
import server.events.CommunityUpdated;

public abstract class Street extends AbstractPokerState {
    protected AbstractPokerState nextState;

    @Override
    public void goNext(Game game) {
        dealer.burnCard();
        game.sendMessage(new CommunityUpdated(dealer.dealCommunityCard()));
        game.getBettingStructure().reach(this);
        game.setNextState(nextState);
    }
}
