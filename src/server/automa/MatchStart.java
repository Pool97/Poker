package server.automa;

import events.Events;
import events.PlayerAddedEvent;
import interfaces.PokerState;
import server.model.PlayerModel;
import server.model.Room;

import java.util.ArrayList;
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
        Room room = match.getRoom();
        match.getMatchModel().setPositions(room.getSize());

        setInitialPositions();
        CountDownLatch countdown = new CountDownLatch(1);
        match.getMatchModel().setStartChips(room.getSize() * 10000);
        match.getMatchModel().setInitialBlinds();

        Events startEvents = new Events();
        room.getOrderedPlayers()
                .forEach(player -> startEvents.addEvent(new PlayerAddedEvent(player.getNickname(), player.getAvatarFilename(), player.getPosition(),
                        player.getTotalChips())));

        room.sendBroadcast(countdown, startEvents);

        try {
            countdown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        match.setState(new TurnStart(match));
    }

    /**
     * Permette di impostare la posizione iniziale di ogni giocatore. Il criterio è che le posizioni vengono
     * assegnate in base all'ordine cronologico di connessione dei Player alla partita.
     */

    public void setInitialPositions() {
        ArrayList<PlayerModel> playersList = new ArrayList<>(match.getRoom().getPlayers());
        for (int i = 0; i < match.getRoom().getSize(); i++)
            playersList.get(i).setPosition(match.getMatchModel().getPosition(i));
    }
}