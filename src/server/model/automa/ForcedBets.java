package server.model.automa;

import server.controller.Game;
import server.events.PlayerUpdated;
import server.events.PotUpdated;
import server.model.PlayerModel;
import server.model.Position;
import server.model.automa.round.LimitRound;
import server.model.automa.round.NoLimitRound;
import server.model.gamestructure.FixedLimit;
import server.model.gamestructure.NoLimit;

import java.util.ListIterator;

public class ForcedBets extends AbstractPokerState {
    private AbstractPokerState nextState;
    private int bigBlind;

    @Override
    public void goNext(Game game) {
        PlayerModel playerModel;
        bigBlind = game.getBigBlind();
        for(PlayerModel player : table){
            game.sendMessage(new PlayerUpdated(player.getNickname(), player.getChips(), "PAY",
                            dealer.collectForcedBetFrom(player, game.getAnte())));
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ListIterator<PlayerModel> iterator = table.iterator();

        playerModel = iterator.next();

        payBlindAndUpdate(game, playerModel, dealer.collectForcedBetFrom(playerModel, game.getSmallBlind()));
        game.sendMessage(new PotUpdated(dealer.getPotValue()));

        playerModel = iterator.next();


        payBlindAndUpdate(game, playerModel, dealer.collectForcedBetFrom(playerModel, game.getBigBlind()));
        game.sendMessage(new PotUpdated(dealer.getPotValue()));
        game.getBettingStructure().reach(this);
        game.setNextState(nextState);
    }

    private void payBlindAndUpdate(Game game, PlayerModel player, int value) {
        PlayerUpdated event = new PlayerUpdated(player.getNickname(), player.getChips(), "PAY ", value);
        game.sendMessage(event);
    }

    @Override
    public void nextState(NoLimit type) {
        nextState = new NoLimitRound((Position.BB.ordinal() + 1) % table.size(), Position.BB.ordinal(), 0);
        nextState.setTransitionStrategy(Flop::new);
    }

    @Override
    public void nextState(FixedLimit type) {
        nextState = new LimitRound(bigBlind, (Position.BB.ordinal() + 1) % table.size(), Position.BB.ordinal(), 0);
        nextState.setTransitionStrategy(Flop::new);
    }
}
