package server.model.automa;

import server.events.EventsContainer;
import server.events.PlayerUpdatedEvent;
import server.events.PotUpdatedEvent;
import server.model.PlayerModel;
import server.model.Position;

public class ForcedBets extends AbstractPokerState {

    @Override
    public void goNext(Game game) {
        PlayerModel playerModel;

        playerModel = table.getPlayer(Position.SB);

        dealer.collectForcedBetFrom(playerModel);
        payBlindAndUpdate(game, playerModel, dealer.getTurnBetOf(playerModel.getNickname()));
        game.sendMessage(new EventsContainer(new PotUpdatedEvent(dealer.getPotValue())));

        playerModel = table.getPlayer(Position.BB);

        dealer.collectForcedBetFrom(playerModel);
        payBlindAndUpdate(game, playerModel, dealer.getTurnBetOf(playerModel.getNickname()));
        game.sendMessage(new EventsContainer(new PotUpdatedEvent(dealer.getPotValue())));

        game.setState(new FirstBettingRound());
    }

    private void payBlindAndUpdate(Game game, PlayerModel player, int value) {
        PlayerUpdatedEvent event = new PlayerUpdatedEvent(player.getNickname(), player.getChips(), "PAY " + value);
        game.sendMessage(new EventsContainer(event));
    }

}
