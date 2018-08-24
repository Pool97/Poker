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
        Events events = new Events();
        PotHandler pot = new PotHandler(match.getRoom().getPlayers(), match.getTurnModel().getCommunityModel());
        String nicknameWinner;
        if (match.getRoom().getPlayers()
                .stream()
                .filter(playerModel -> !playerModel.hasLost())
                .filter(player -> !player.hasFolded())
                .count() == 1) {
            nicknameWinner = match.getRoom().getPlayers()
                    .stream()
                    .filter(playerModel -> !playerModel.hasLost())
                    .filter(player -> !player.hasFolded()).findFirst().get().getNickname();
            System.out.println("Il vincitore stavolta e': " + nicknameWinner);
        } else {
            nicknameWinner = pot.evaluateTurnWinner();
            events.addEvent(new ShowdownEvent(pot.getPlayersHandByName()));
        }

        pot.assignPots(nicknameWinner);

        Room room = match.getRoom();

        room.getPlayers().stream().filter(playerModel -> !playerModel.hasLost()).forEach(player -> events.addEvent(new PlayerUpdatedEvent(player.getNickname(), player.getChips())));

        room.sendBroadcast(events);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        match.setState(new TurnEnd(match));
    }
}
