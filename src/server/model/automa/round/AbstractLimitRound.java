package server.model.automa.round;

import client.events.ActionPerformed;
import interfaces.Event;
import server.controller.Game;
import server.events.PlayerDisconnected;
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

public abstract class AbstractLimitRound extends BettingRound {
    private List<AbstractPokerAction> turnActions;
    private int fixedLimit;

    public AbstractLimitRound(int fixedLimit){
        turnActions = new ArrayList<>();
        this.fixedLimit = fixedLimit;
    }

    protected void doAction(PlayerModel player, Game game) {
        PlayerRound optionsEvent;
        int currentBet = dealer.getPotMatchingValue(player.getNickname(), game.getBigBlind());

        actionGenerator = new LimitActionGenerator(fixedLimit, getNumberOfRaiseBy(player.getNickname()),
                (int)getNumberOfBetBy(player.getNickname()), currentBet, player);

        optionsEvent = generateActions();
        optionsEvent.setPlayerNickname(player.getNickname());
        optionsEvent.getOptions().forEach(System.out::println);

        game.sendMessage(optionsEvent);

        Event userResponse = game.readMessage(player.getNickname());
        if(!(userResponse instanceof PlayerDisconnected)) {
            ActionPerformed playerAction = (ActionPerformed) userResponse;
            playerAction.getAction().accept(this);
            playerAction.getAction().setNickname(player.getNickname());
            dealer.collectAction(player, playerAction.getAction().getValue());
            game.sendMessage(new PlayerUpdated(player.getNickname(), player.getChips(),
                    playerAction.getAction().toString(), playerAction.getAction().getValue()));
            game.sendMessage(new PotUpdated(table.getPotValue()));
            turnActions.add(playerAction.getAction());
            if (table.getPlayerByName(player.getNickname()).getChips() == 0)
                player.setAllIn(true);
        }else{
            table.getPlayerByName(player.getNickname()).setDisconnected(true);
            table.removeDisconnectedPlayers();
            game.sendMessage(userResponse);
        }
    }

    public int getNumberOfRaiseBy(String nickname){
        return (int)turnActions.stream().filter(action -> action.getNickname().equals(nickname))
                .filter(action -> action instanceof RaiseLimit)
                .count();
    }

    public long getNumberOfBetBy(String nickname) {
        return turnActions.stream().filter(action -> action.getNickname().equals(nickname))
                .filter(action -> action instanceof BetLimit).count();

    }
}
