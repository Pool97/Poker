package server.model.automa;

import server.events.EventsContainer;
import server.events.MatchLostEvent;
import server.events.PotUpdatedEvent;
import server.events.TurnEndedEvent;
import server.model.PlayerModel;

public class TurnEnd extends AbstractPokerState{

    public TurnEnd() {
    }

    @Override
    public void goNext(Game game) {
        System.out.println("TurnEnd.");

        EventsContainer eventsContainer = new EventsContainer();
        eventsContainer.addEvent(new PotUpdatedEvent(dealer.getPotValue()));
        table.getPlayers().stream().filter(playerModel -> playerModel.getChips() <= 0).forEach(playerModel -> playerModel.setLost(true));
        table.getPlayers().forEach(PlayerModel::giveBackCards);
        table.getPlayers().stream()
                .filter(PlayerModel::hasLost)
                .forEach(playerModel -> eventsContainer.addEvent(new MatchLostEvent(playerModel.getNickname(),
                        table.currentNumberOfPlayers() + 1, playerModel.isCreator())));
        table.getPlayers().stream().filter(PlayerModel::hasLost).forEach(playerModel -> table.removePosition());

        eventsContainer.addEvent(new TurnEndedEvent());

        game.sendMessage(eventsContainer);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dealer.emptyCommunity();
        table.refreshLists();
        game.setState(new StartTurn());
    }

}
