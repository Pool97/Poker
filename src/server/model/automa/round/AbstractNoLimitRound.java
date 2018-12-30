package server.model.automa.round;

import client.events.ActionPerformed;
import server.controller.Game;
import server.events.PlayerRound;
import server.events.PlayerUpdated;
import server.events.PotUpdated;
import server.model.PlayerModel;
import server.model.actions.AllIn;
import server.model.actions.BetNoLimit;
import server.model.actions.Fold;
import server.model.actions.RaiseNoLimit;
import server.model.gamestructure.NoLimitActionGenerator;

import java.util.AbstractMap;

public abstract class AbstractNoLimitRound extends BettingRound {
    private PlayerModel actualPlayer;
    private ActionPerformed action;
    private int currentBet;

    protected void doAction(PlayerModel player, Game game) {
        actualPlayer = player;
        currentBet = dealer.getPotMatchingValue(player.getNickname(), game.getBigBlind());

        actionGenerator = new NoLimitActionGenerator(dealer.getMinimumLegalRaise(), player,
                currentBet);

        PlayerRound optionsEvent = generateActions();
        optionsEvent.setPlayerNickname(player.getNickname());

        game.sendMessage(optionsEvent);

        action = (ActionPerformed) game.readMessage(player.getNickname());

        dealer.collectAction(player, action.getAction().getValue());
        action.getAction().accept(this);

        game.sendMessage(new PlayerUpdated(actualPlayer.getNickname(), actualPlayer.getChips(),
                action.getAction().toString(), action.getAction().getValue()));
        game.sendMessage(new PotUpdated(table.getPotValue()));

        if(table.getPlayerByName(player.getNickname()).getChips() == 0)
            player.setAllIn(true);
    }


    @Override
    public void process(Fold fold) {
        System.out.println("FOLDED");
        actualPlayer.setFolded(true);
    }

    @Override
    public void process(BetNoLimit betNoLimit) {
        if(action.getAction().getValue() > dealer.getMinimumLegalRaise().getValue())
            dealer.setMinimumLegalRaise(new AbstractMap.SimpleEntry<>(actualPlayer.getNickname(),
                action.getAction().getValue()));
    }

    @Override
    public void process(RaiseNoLimit raiseNoLimit) {
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
