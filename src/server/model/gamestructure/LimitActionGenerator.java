package server.model.gamestructure;

import interfaces.ActionGenerator;
import interfaces.PokerAction;
import server.model.PlayerModel;
import server.model.actions.*;

public class LimitActionGenerator implements ActionGenerator {
    private int fixedLimit;
    private int raiseHappened;
    private int betHappened;
    private int currentBet;
    private PlayerModel playerModel;

    public LimitActionGenerator(int fixedLimit, int raiseHappened, int betHappened, int currentBet, PlayerModel playerModel){
        this.fixedLimit = fixedLimit;
        this.raiseHappened = raiseHappened;
        this.betHappened = betHappened;
        this.currentBet = currentBet;
        this.playerModel = playerModel;
    }

    @Override
    public PokerAction retrieveRaise() {
        if(raiseHappened < 3)
            if(currentBet == 0)
                return NullAction.getInstance();
            if(currentBet + fixedLimit < playerModel.getChips())
                return new RaiseLimit(currentBet + fixedLimit);
        return NullAction.getInstance();
    }

    @Override
    public PokerAction retrieveCall() {
        if(currentBet < playerModel.getChips() && currentBet != 0)
            return new Call(currentBet);
        return NullAction.getInstance();
    }

    @Override
    public PokerAction retrieveBet() {
        if(betHappened == 0)
            if(currentBet == 0 && fixedLimit < playerModel.getChips())
                return new BetLimit(fixedLimit);
        return NullAction.getInstance();
    }

    @Override
    public PokerAction retrieveFold() {
        return !playerModel.isAllIn() ? new Fold() : NullAction.getInstance();
    }

    @Override
    public PokerAction retrieveCheck() {
        if(currentBet == 0)
            return new Check();
        return NullAction.getInstance();
    }

    @Override
    public PokerAction retrieveAllIn() {
        if(currentBet >= playerModel.getChips())
            return new AllIn(playerModel.getChips(), false);
        else if(currentBet + fixedLimit == playerModel.getChips())
            return new AllIn(playerModel.getChips(), true);
        return NullAction.getInstance();
    }
}
