package server.model.automa.round;

import client.events.ActionPerformedEvent;
import server.events.EventsContainer;
import server.events.PlayerRoundEvent;
import server.model.PlayerModel;
import server.model.actions.AbstractPokerAction;
import server.model.actions.BetLimit;
import server.model.actions.RaiseLimit;
import server.model.automa.Game;
import server.model.gamestructure.LimitActionGenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class LimitRound extends BettingRound {
    List<AbstractPokerAction> turnActions;
    protected int numberOfRaiseHappened;
    protected int numberOfBetHappened;
    private int fixedLimit;

    public LimitRound(int fixedLimit){
        turnActions = new ArrayList<>();
        this.fixedLimit = fixedLimit;
    }

    protected void doAction(PlayerModel player, Game game) {
        PlayerRoundEvent optionsEvent;
        int currentBet = dealer.getPotMatchingValue(player.getNickname(), game.getBigBlind());

        actionGenerator = new LimitActionGenerator(fixedLimit, getNumberOfRaiseBy(player.getNickname()),
                getNumberOfBetBy(player.getNickname()), currentBet, player);

        optionsEvent = generateActions();
        optionsEvent.setPlayerNickname(player.getNickname());

        game.sendMessage(new EventsContainer(optionsEvent));

        ActionPerformedEvent playerAction = (ActionPerformedEvent) game.readMessage(player.getNickname()).getEvent();
        playerAction.getAction().accept(this);
        dealer.collectAction(player, playerAction.getAction().getValue());
        turnActions.add(playerAction.getAction());
        if(table.getPlayerByName(player.getNickname()).getChips() == 0)
            player.setAllIn(true);
    }

    @Override
    public void process(RaiseLimit raiseNoLimitOption) {
        numberOfRaiseHappened++;
    }

    @Override
    public void process(BetLimit betLimit) {
        numberOfBetHappened++;
    }

    public int getNumberOfRaiseBy(String nickname){
        return (int)turnActions.stream().filter(action -> action.getNickname().equals(nickname))
                .filter(action -> action instanceof RaiseLimit)
                .count();
    }

    public int getNumberOfBetBy(String nickname){
        return (int)turnActions.stream().filter(action -> action.getNickname().equals(nickname))
                .filter(action -> action instanceof BetLimit)
                .count();
    }
}
