package server.model.gamestructure;

public class NoLimit extends BettingStructure {

    public NoLimit(int smallBlind){
        super(smallBlind, smallBlind * 2);
    }


    @Override
    public void increaseBlinds() {
        bigBlind *= 2;
        smallBlind = bigBlind / 2;
    }
}
