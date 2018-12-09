package server.model.gamestructure;

import interfaces.Reachable;
import interfaces.Reacher;

public class FixedLimit extends BettingStructure implements Reachable {
    private int lowerLimit;
    private int higherLimit;

    public FixedLimit(int lowerLimit){
        super(lowerLimit / 2, lowerLimit * 2);

        this.lowerLimit = lowerLimit;
        this.higherLimit = lowerLimit * 2;
    }

    @Override
    public void increaseBlinds() {
        bigBlind *= 2;
        smallBlind = bigBlind / 2;
    }

    @Override
    public void reach(Reacher reacher) {
        reacher.nextState(this);
    }
}