package server.automa;

import interfaces.PokerState;
import server.controller.Context;
import server.controller.Room;
import server.events.EventsContainer;
import server.events.PlayerLoggedEvent;
import server.events.RoomCreatedEvent;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * StartGame è il primo stato dell'automa.
 * <p>
 * In questo stato devono essere compiute le seguenti azioni:
 * - Stabilire le posizioni iniziali dei giocatori ({@link server.model.Position});
 * - Impostare il numero iniziale di Chips per ogni giocatore
 * - Informare tutti i giocatori dei rispettivi avversari
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class StartGame implements PokerState {
    private static final String STATE_STARTED = "Lo stato di StartGame è avviato.\n";
    private static final String START_MATCH = "La partità può iniziare. \n";
    private static final String CONF_MATCH = "Configurazione ottimale del MatchHandler in corso... \n";
    private Context match;
    private Room room;

    public StartGame(Context match) {
        this.match = match;
        this.room = match.getRoom();
        match.setInitialParameters(calculateStartChips());
    }

    @Override
    public void goNext() {
        Context.logger.info(STATE_STARTED);
        Context.logger.info(CONF_MATCH);

        configureRoom();
        sendEventsToPlayers();

        Context.logger.info(START_MATCH);

        match.setState(new StartTurn(match));
    }

    private void configureRoom() {
        room.setPlayersChips(calculateStartChips());
        room.assignInitialPositions();
        room.sortPlayersByPosition();
    }

    private void sendEventsToPlayers() {
        room.sendBroadcast(new EventsContainer(new RoomCreatedEvent()));
        room.sendBroadcast(preparePlayersLoggedEvents());
    }

    private int calculateStartChips() {
        return room.getSize() * 10000;
    }

    private EventsContainer preparePlayersLoggedEvents() {
        ArrayList<PlayerLoggedEvent> loggedEvents = room.getPlayers()
                .stream()
                .map(player -> new PlayerLoggedEvent(player.getNickname(), player.getAvatar(), player.getPosition(),
                        player.getChips()))
                .collect(Collectors.toCollection(ArrayList::new));
        return new EventsContainer(loggedEvents);
    }
}