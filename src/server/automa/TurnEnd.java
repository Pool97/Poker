package server.automa;

import interfaces.PokerState;
import server.controller.Context;
import server.controller.Room;
import server.events.EventsContainer;
import server.events.PlayerHasLostEvent;
import server.events.PotUpdatedEvent;
import server.events.TurnEndedEvent;
import server.model.PlayerModel;

public class TurnEnd implements PokerState {
    private Context match;

    public TurnEnd(Context match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        System.out.println("TurnEnd.");
        EventsContainer eventsContainer = new EventsContainer();
        match.getTurnModel().resetPot();
        eventsContainer.addEvent(new PotUpdatedEvent(match.getTurnModel().getPot()));
        Room room = match.getRoom();
        match.getRoom().getPlayers().stream().filter(playerModel -> playerModel.getChips() <= 0).forEach(playerModel -> playerModel.setLost(true));
        match.getRoom().getPlayers().forEach(PlayerModel::removeCards);
        match.getRoom().getPlayers().forEach(PlayerModel::removeActions);
        match.getRoom().getPlayers().stream()
                .filter(PlayerModel::hasLost)
                .forEach(playerModel -> eventsContainer.addEvent(new PlayerHasLostEvent(playerModel.getNickname())));
        match.getRoom().getPlayers().stream().filter(PlayerModel::hasLost).forEach(playerModel -> room.removePosition());
        eventsContainer.addEvent(new TurnEndedEvent());
        room.sendBroadcast(eventsContainer);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        room.refreshLists();
        match.setState(new StartTurn(match));


    }

}
