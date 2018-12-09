package server.model.automa.round;

import client.events.ActionPerformedEvent;
import server.events.EventsContainer;
import server.events.PlayerRoundEvent;
import server.events.PlayerUpdatedEvent;
import server.events.PotUpdatedEvent;
import server.model.PlayerModel;
import server.model.actions.AllIn;
import server.model.actions.BetNoLimit;
import server.model.actions.Fold;
import server.model.actions.RaiseNoLimit;
import server.model.automa.Game;
import server.model.gamestructure.NoLimitActionGenerator;

import java.util.AbstractMap;

public abstract class NoLimitRound extends BettingRound {
    private PlayerModel actualPlayer;
    private ActionPerformedEvent action;
    private int currentBet;

    protected void doAction(PlayerModel player, Game game) {
        actualPlayer = player;
        currentBet = dealer.getPotMatchingValue(player.getNickname(), game.getBigBlind());

        actionGenerator = new NoLimitActionGenerator(dealer.getMinimumLegalRaise(), player,
                currentBet);

        PlayerRoundEvent optionsEvent = generateActions();
        optionsEvent.setPlayerNickname(player.getNickname());

        game.sendMessage(new EventsContainer(optionsEvent));

        action = (ActionPerformedEvent) game.readMessage(player.getNickname()).getEvent();

        dealer.collectAction(player, action.getAction().getValue());
        action.getAction().accept(this);

        game.sendMessage(new EventsContainer(new PlayerUpdatedEvent(actualPlayer.getNickname(), actualPlayer.getChips(),
                action.getAction().getClass().getSimpleName(), action.getAction().getValue()), new PotUpdatedEvent(table.getPotValue())));

        if(table.getPlayerByName(player.getNickname()).getChips() == 0)
            player.setAllIn(true);
    }


    @Override
    public void process(Fold fold) {
        actualPlayer.setFolded(true);
    }

    @Override
    public void process(BetNoLimit betNoLimit) {
        if(action.getAction().getValue() > dealer.getMinimumLegalRaise().getValue())
            dealer.setMinimumLegalRaise(new AbstractMap.SimpleEntry<>(actualPlayer.getNickname(),
                action.getAction().getValue()));
    }

    @Override
    public void process(RaiseNoLimit raiseNoLimitOption) {
        if(action.getAction().getValue() - currentBet > dealer.getMinimumLegalRaise().getValue())
            dealer.setMinimumLegalRaise(new AbstractMap.SimpleEntry<>(actualPlayer.getNickname(),
                action.getAction().getValue() - currentBet));
    }

    @Override
    public void process(AllIn allin) {
        if(allin.getValue() - currentBet > dealer.getMinimumLegalRaise().getValue())
            dealer.setMinimumLegalRaise(new AbstractMap.SimpleEntry<>(actualPlayer.getNickname(),
                    action.getAction().getValue() - currentBet));
    }
}
