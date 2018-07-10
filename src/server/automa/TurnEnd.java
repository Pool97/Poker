package server.automa;

import interfaces.PokerState;
import server.events.Events;
import server.events.PotUpdatedEvent;
import server.events.TurnEndedEvent;
import server.model.PlayerModel;
import server.model.Room;

public class TurnEnd implements PokerState {
    private MatchHandler match;

    public TurnEnd(MatchHandler match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        System.out.println("TurnEnd.");
        Events events = new Events();
        match.getTurnModel().resetPot();
        events.addEvent(new PotUpdatedEvent(match.getTurnModel().getPot()));
        Room room = match.getRoom();
        match.getRoom().getPlayers().forEach(PlayerModel::removeCards);
        match.getRoom().getPlayers().forEach(PlayerModel::removeActions);
        events.addEvent(new TurnEndedEvent());
        room.sendBroadcast(events);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        match.setState(new StartTurn(match));

    }

}
