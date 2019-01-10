package server.model.gamestructure;

import interfaces.Reachable;
import interfaces.Reacher;

public class NoLimit extends BettingStructure implements Reachable {

    public NoLimit(int smallBlind){
        super(smallBlind, smallBlind * 2);
    }


    @Override
    public void increaseBlinds() {
        bigBlind = bigBlind + bigBlind / 2;
        smallBlind = bigBlind / 2;
    }

    @Override
    public void reach(Reacher reacher) {
        reacher.nextState(this);
    }
}
