package server.model.automa;

import server.events.EventsContainer;
import server.events.PlayerHasLostEvent;
import server.events.PotUpdatedEvent;
import server.events.TurnEndedEvent;
import server.model.PlayerModel;

public class TurnEnd extends AbstractPokerState{

    public TurnEnd() {
    }

    @Override
    public void goNext(Context context) {
        System.out.println("TurnEnd.");
        EventsContainer eventsContainer = new EventsContainer();
        table.resetPot();
        eventsContainer.addEvent(new PotUpdatedEvent(table.getPot()));
        table.getPlayers().stream().filter(playerModel -> playerModel.getChips() <= 0).forEach(playerModel -> playerModel.setLost(true));
        table.getPlayers().forEach(PlayerModel::removeCards);
        table.getPlayers().forEach(PlayerModel::removeActions);
        table.getPlayers().stream()
                .filter(PlayerModel::hasLost)
                .forEach(playerModel -> eventsContainer.addEvent(new PlayerHasLostEvent(playerModel.getNickname())));
        table.getPlayers().stream().filter(PlayerModel::hasLost).forEach(playerModel -> table.removePosition());
        eventsContainer.addEvent(new TurnEndedEvent());
        table.sendBroadcast(eventsContainer);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        table.refreshLists();
        context.setState(new StartTurn());


    }

}
