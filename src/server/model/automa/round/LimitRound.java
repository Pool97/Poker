package server.model.automa.round;

import client.events.ActionPerformed;
import server.controller.Game;
import server.events.PlayerRound;
import server.events.PlayerUpdated;
import server.events.PotUpdated;
import server.model.PlayerModel;
import server.model.actions.AbstractPokerAction;
import server.model.actions.BetLimit;
import server.model.actions.RaiseLimit;
import server.model.gamestructure.LimitActionGenerator;

import java.util.ArrayList;
import java.util.List;

public abstract class LimitRound extends BettingRound {
    private List<AbstractPokerAction> turnActions;
    protected int numberOfRaiseHappened;
    protected int numberOfBetHappened;
    private int fixedLimit;

    public LimitRound(int fixedLimit){
        turnActions = new ArrayList<>();
        this.fixedLimit = fixedLimit;
    }

    protected void doAction(PlayerModel player, Game game) {
        PlayerRound optionsEvent;
        int currentBet = dealer.getPotMatchingValue(player.getNickname(), game.getBigBlind());

        actionGenerator = new LimitActionGenerator(fixedLimit, getNumberOfRaiseBy(player.getNickname()),
                getNumberOfBetBy(player.getNickname()), currentBet, player);

        optionsEvent = generateActions();
        optionsEvent.setPlayerNickname(player.getNickname());
        optionsEvent.getOptions().forEach(System.out::println);

        game.sendMessage(optionsEvent);

        ActionPerformed playerAction = (ActionPerformed) game.readMessage(player.getNickname());
        playerAction.getAction().accept(this);
        playerAction.getAction().setNickname(player.getNickname());

        dealer.collectAction(player, playerAction.getAction().getValue());
        game.sendMessage(new PlayerUpdated(player.getNickname(), player.getChips(),
                playerAction.getAction().getClass().getSimpleName(), playerAction.getAction().getValue()));
        game.sendMessage(new PotUpdated(table.getPotValue()));
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
        if(turnActions.size() == 0)
            return 0;
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
