package server.model.gamestructure;

import interfaces.ActionGenerator;
import interfaces.PokerAction;
import server.model.PlayerModel;
import server.model.actions.*;

import java.util.Map;

public class NoLimitActionGenerator implements ActionGenerator {
    private Map.Entry<String, Integer> minimumLegalRaise;
    private PlayerModel playerModel;
    private int currentBet;

    public NoLimitActionGenerator(Map.Entry<String, Integer> minimumLegalRaise, PlayerModel playerModel, int currentBet){
        this.minimumLegalRaise = minimumLegalRaise;
        this.playerModel = playerModel;
        this.currentBet = currentBet;
    }

    @Override
    public PokerAction retrieveRaise() {
        if(currentBet == 0)
            return NullAction.getInstance();

        if(currentBet + minimumLegalRaise.getValue() < playerModel.getChips())
            return new RaiseNoLimit(currentBet + minimumLegalRaise.getValue());

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
        if(currentBet > 0 || playerModel.getChips() < minimumLegalRaise.getValue()) {
            return NullAction.getInstance();
        }

        return new BetNoLimit(minimumLegalRaise.getValue());
    }

    @Override
    public PokerAction retrieveFold() {
        if(!playerModel.isAllIn())
            return new Fold();
        return NullAction.getInstance();
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
        return new AllIn(playerModel.getChips(), true);
    }
}
