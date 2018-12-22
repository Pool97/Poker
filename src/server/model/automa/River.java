package server.model.automa;

import interfaces.PokerState;
import server.controller.Game;
import server.events.CommunityUpdated;
import server.model.automa.round.NextLimitRound;
import server.model.automa.round.NextNoLimitRound;
import server.model.gamestructure.FixedLimit;
import server.model.gamestructure.NoLimit;

public class River extends Street implements PokerState {
    private AbstractPokerState nextState;
    private int fixedLimit;

    @Override
    public void goNext(Game game) {
        dealer.burnCard();
        fixedLimit = game.getBigBlind() * 2;
        game.sendMessage(new CommunityUpdated(dealer.dealCommunityCard()));
        game.getBettingStructure().reach(this);
        game.setState(nextState);
    }

    @Override
    public void nextState(NoLimit type) {
        nextState = new NextNoLimitRound();
        ((NextNoLimitRound) nextState).setTransitionStrategy(Showdown::new);
    }

    @Override
    public void nextState(FixedLimit type) {
        nextState = new NextLimitRound(fixedLimit);
        ((NextLimitRound) nextState).setTransitionStrategy(Showdown::new);
    }
}
