package server.model.automa;

import server.events.EventsContainer;
import server.events.PlayerUpdatedEvent;
import server.events.PotUpdatedEvent;
import server.model.PlayerModel;

import java.util.ListIterator;

public class ForcedBets extends AbstractPokerState {

    @Override
    public void goNext(Game game) {
        PlayerModel playerModel;

        for(PlayerModel player : table){
            dealer.collectForcedBetFrom(player, game.getAnte());
            game.sendMessage(new EventsContainer(
                    new PlayerUpdatedEvent(player.getNickname(), player.getChips(), "PAY" + game.getAnte())));
        }

        ListIterator<PlayerModel> iterator = table.iterator();

        playerModel = iterator.next();

        dealer.collectForcedBetFrom(playerModel, game.getSmallBlind());
        payBlindAndUpdate(game, playerModel, dealer.getTurnBetOf(playerModel.getNickname()));
        game.sendMessage(new EventsContainer(new PotUpdatedEvent(dealer.getPotValue())));

        playerModel = iterator.next();

        dealer.collectForcedBetFrom(playerModel, game.getBigBlind());
        payBlindAndUpdate(game, playerModel, dealer.getTurnBetOf(playerModel.getNickname()));
        game.sendMessage(new EventsContainer(new PotUpdatedEvent(dealer.getPotValue())));

        game.setState(new FirstNoLimitRound());
    }

    private void payBlindAndUpdate(Game game, PlayerModel player, int value) {
        PlayerUpdatedEvent event = new PlayerUpdatedEvent(player.getNickname(), player.getChips(), "PAY " + value);
        game.sendMessage(new EventsContainer(event));
    }

}
