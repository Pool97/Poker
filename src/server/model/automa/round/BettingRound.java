package server.model.automa.round;

import interfaces.ActionGenerator;
import interfaces.ActionManager;
import server.events.PlayerRound;
import server.model.actions.*;
import server.model.automa.AbstractPokerState;

public abstract class BettingRound extends AbstractPokerState implements ActionManager {
    protected ActionGenerator actionGenerator;
    protected int roundNumber;
    protected int lastRaiseOrBetCursor;

    @Override
    public void process(Call call) {

    }

    @Override
    public void process(RaiseNoLimit raiseNoLimit) {
        lastRaiseOrBetCursor = table.getPlayerPosition(raiseNoLimit.getNickname());
    }

    @Override
    public void process(Fold fold) {
    }

    @Override
    public void process(Check check) {

    }

    @Override
    public void process(BetNoLimit betNoLimit) {
        System.out.println("Entrato!");
        System.out.println(betNoLimit.getNickname());
        lastRaiseOrBetCursor = table.getPlayerPosition(betNoLimit.getNickname());
    }

    @Override
    public void process(AllIn allin) {

    }

    @Override
    public void process(RaiseLimit raiseLimit) {
        lastRaiseOrBetCursor = table.getPlayerPosition(raiseLimit.getNickname());
    }

    @Override
    public void process(BetLimit betLimit) {
        lastRaiseOrBetCursor = table.getPlayerPosition(betLimit.getNickname());
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

    protected boolean roundFinished(int cursor){
        return roundNumber >= 1 && (table.countPlayersInGame() == 1 || ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) && !checkForActingPlayer())
                || table.isAllPlayersAllIn() || (cursor == lastRaiseOrBetCursor && isMatched()));
    }

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
