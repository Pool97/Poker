package server.automa;

import interfaces.PokerState;
import server.events.Events;
import server.events.PlayerUpdatedEvent;
import server.events.ShowdownEvent;
import server.model.PotHandler;
import server.model.Room;

public class Showdown implements PokerState {
    private MatchHandler match;

    public Showdown(MatchHandler match) {
        this.match = match;
    }

    @Override
    public void goNext() {
        System.out.println("ShowDown.");

        PotHandler pot = new PotHandler(match.getRoom().getPlayers(), match.getTurnModel().getCommunityModel());
        String nicknameWinner = pot.evaluateTurnWinner();
        pot.assignPots(nicknameWinner);

        Room room = match.getRoom();
        Events events = new Events();
        room.getPlayers().forEach(player -> events.addEvent(new PlayerUpdatedEvent(player.getNickname(), player.getChips())));
        events.addEvent(new ShowdownEvent(pot.getPlayersHandByName()));
        room.sendBroadcast(events);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        match.setState(new TurnEnd(match));
    }
}
