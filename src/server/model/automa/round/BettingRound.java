package server.model.automa.round;

import interfaces.ActionGenerator;
import interfaces.ActionManager;
import server.events.PlayerRound;
import server.model.actions.*;
import server.model.automa.AbstractPokerState;

public abstract class BettingRound extends AbstractPokerState implements ActionManager {
    protected ActionGenerator actionGenerator;

    @Override
    public void process(Call call) {

    }

    @Override
    public void process(RaiseNoLimit raiseNoLimitOption) {

    }

    @Override
    public void process(Fold fold) {

    }

    @Override
    public void process(Check check) {

    }

    @Override
    public void process(BetNoLimit betNoLimit) {

    }

    @Override
    public void process(AllIn allin) {

    }

    @Override
    public void process(RaiseLimit raiseLimit) {

    }

    @Override
    public void process(BetLimit betLimit) {

    }

    protected boolean checkForActingPlayer() {
        return table.getActivePlayers()
                .stream()
                .anyMatch(player -> dealer.getTurnBetOf(player.getNickname()) < dealer.maxBetAmong(table.getAllInPlayers()));
    }

    protected boolean isMatched() {
        if (checkForActingPlayer())
            return false;

        return dealer.isBetPossible(table.getActivePlayers());
    }

    @Override
    public void process(AbstractPokerAction action) {

    }

    protected abstract boolean roundFinished(int cursor);

    protected PlayerRound generateActions(){
        PlayerRound actions = new PlayerRound();
        actions.addOption(actionGenerator.retrieveCall());
        actions.addOption(actionGenerator.retrieveCheck());
        actions.addOption(actionGenerator.retrieveFold());
        actions.addOption(actionGenerator.retrieveBet());
        actions.addOption(actionGenerator.retrieveRaise());
        actions.addOption(actionGenerator.retrieveAllIn());
        return actions;
    }
}
