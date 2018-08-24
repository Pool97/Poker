package server.automa;

import interfaces.PokerState;
import server.events.Events;
import server.events.PlayerHasLostEvent;
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
        match.getRoom().getPlayers().stream().filter(playerModel -> playerModel.getChips() <= 0).forEach(playerModel -> playerModel.setLost(true));
        match.getRoom().getPlayers().forEach(PlayerModel::removeCards);
        match.getRoom().getPlayers().forEach(PlayerModel::removeActions);
        match.getRoom().getPlayers().stream()
                .filter(PlayerModel::hasLost)
                .forEach(playerModel -> events.addEvent(new PlayerHasLostEvent(playerModel.getNickname())));
        match.getRoom().getPlayers().stream().filter(PlayerModel::hasLost).forEach(playerModel -> room.removePosition());
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
