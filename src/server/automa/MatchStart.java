package server.automa;

import events.Events;
import events.PlayerAddedEvent;
import interfaces.PokerState;
import server.model.Room;

import java.util.concurrent.CountDownLatch;

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
        Room room = match.getServerManager().getRoom();
        room.setInitialPositions();
        CountDownLatch countdown = new CountDownLatch(1);
        Events startEvents = new Events();
        match.getMatchModel().setStartChips(room.getSize() * 10000);
        match.getMatchModel().setInitialBlinds();
        room.getPlayers()
                .forEach(player -> startEvents.addEvent(new PlayerAddedEvent(player.getNickname(), player.getAvatarFilename(), player.getPosition(),
                        player.getTotalChips())));
        match.getServerManager().sendMessage(room.getConnections(), countdown, startEvents);
        try {
            countdown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            match.setState(new TurnStart(match));
        }
    }
}
