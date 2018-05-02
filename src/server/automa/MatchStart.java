package server.automa;

import events.Events;
import events.PlayerAddedEvent;
import interfaces.PokerState;
import server.model.Room;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * MatchStart è il primo stato dell'automa.
 * <p>
 * In questo stato devono essere compiute le seguenti azioni:
 * - Stabilire le posizioni iniziali dei giocatori ({@link server.model.Position});
 * - Informare tutti i giocatori dei rispettivi avversari
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class MatchStart implements PokerState {
    private Match match;

    /**
     * Costruttore della classe MatchStart.
     */

    public MatchStart(Match match) {
        this.match = match;
    }

    /**
     * Vedi {@link PokerState#goNext()}
     */

    @Override
    public void goNext() {
        Room room = match.getRoom();
        match.getMatchModel().setPositions(room.getSize());
        match.getMatchModel().setStartChips(room.getSize() * 10000);
        match.getMatchModel().setInitialBlinds();

        room.setPositions(match.getMatchModel().getPositions());
        room.sort();

        ArrayList<PlayerAddedEvent> events = room.getPlayers()
                .stream()
                .map(player -> new PlayerAddedEvent(player.getNickname(), player.getAvatar(), player.getPosition(), player.getChips()))
                .collect(Collectors.toCollection(ArrayList::new));

        room.sendBroadcast(new Events(events));
        match.setState(new TurnStart(match));
    }
}