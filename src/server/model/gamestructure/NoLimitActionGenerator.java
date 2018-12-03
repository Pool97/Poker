package server.model.gamestructure;

import interfaces.ActionGenerator;
import server.model.PlayerModel;
import server.model.actions.*;

public class NoLimitActionGenerator implements ActionGenerator {
    private int minimumLegalRaise;
    private PlayerModel playerModel;

    @Override
    public RaiseOption retrieveRaise() {
        return new RaiseOption(minimumLegalRaise, playerModel.getChips());
    }

    @Override
    public Call retrieveCall() {
        return null;
    }

    @Override
    public BetOption retrieveBetOption() {
        return null;
    }

    @Override
    public Fold retrieveFold() {
        return null;
    }

    @Override
    public Check retrieveCheck() {
        return null;
    }
}
