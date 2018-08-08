package server.automa;

import interfaces.PokerState;
import server.events.Events;
import server.events.PlayerLoggedEvent;
import server.events.RoomCreatedEvent;
import server.model.MatchModel;
import server.model.Room;

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
    private MatchHandler match;

    /**
     * Costruttore della classe StartGame.
     */

    public StartGame(MatchHandler match) {
        this.match = match;
    }

    /**
     * {@link PokerState#goNext()}
     */

    @Override
    public void goNext() {
        MatchHandler.logger.info(STATE_STARTED);
        MatchHandler.logger.info(CONF_MATCH);

        Room room = match.getRoom();
        MatchModel matchModel = match.getMatchModel();
        matchModel.setStartChips(room.getSize() * 10000);
        room.setPlayersChips(matchModel.getStartChips());
        room.setAvailablePositions(room.getSize());
        matchModel.setInitialBlinds();


        room.setPlayersPositions();
        room.sortByPosition();

        ArrayList<PlayerLoggedEvent> events = room.getPlayers()
                .stream()
                .map(player -> new PlayerLoggedEvent(player.getNickname(), player.getAvatar(), player.getPosition(),
                        player.getChips()))
                .collect(Collectors.toCollection(ArrayList::new));

        System.out.println(room.getPlayers().get(0).getNickname());

        MatchHandler.logger.info(START_MATCH);
        room.sendBroadcast(new Events(new RoomCreatedEvent()));
        room.sendBroadcast(new Events(events));
        match.setState(new StartTurn(match));
    }
}