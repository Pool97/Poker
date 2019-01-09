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
    private int potValue;

    public NoLimitActionGenerator(Map.Entry<String, Integer> minimumLegalRaise, PlayerModel playerModel, int currentBet, int potValue) {
        this.minimumLegalRaise = minimumLegalRaise;
        this.playerModel = playerModel;
        this.currentBet = currentBet;
        this.potValue = potValue;
    }

    @Override
    public PokerAction retrieveRaise() {
        if(currentBet == 0)
            return NullAction.getInstance();

        if (currentBet + minimumLegalRaise.getValue() < playerModel.getChips()) {
            RaiseNoLimit raiseNoLimit = new RaiseNoLimit(currentBet + minimumLegalRaise.getValue());
            raiseNoLimit.setThree(playerModel.getChips() > currentBet + minimumLegalRaise.getValue() * 3 ?
                    currentBet + minimumLegalRaise.getValue() * 3 : 0);
            raiseNoLimit.setHalfPot((playerModel.getChips() > currentBet + potValue / 2) && (currentBet > minimumLegalRaise.getValue()) ? currentBet + potValue / 2 : 0);
            raiseNoLimit.setPot((playerModel.getChips() > currentBet + potValue) && (potValue > minimumLegalRaise.getValue()) ? currentBet + potValue : 0);
            return raiseNoLimit;
        }

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

        BetNoLimit betNoLimit = new BetNoLimit(minimumLegalRaise.getValue());
        betNoLimit.setHalfPot((playerModel.getChips() > potValue / 2 && (potValue / 2) > minimumLegalRaise.getValue()) ? potValue / 2 : 0);
        betNoLimit.setPot((playerModel.getChips() > potValue) && (potValue > minimumLegalRaise.getValue()) ? potValue : 0);
        return betNoLimit;
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
