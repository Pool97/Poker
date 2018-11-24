package server.model.automa;

import server.events.EventsContainer;
import server.events.PlayerLoggedEvent;
import server.events.RoomCreatedEvent;
import server.model.Dealer;
import server.model.Table;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class StartGame extends AbstractPokerState {
    private static final String STATE_STARTED = "Lo stato di StartGame è avviato.\n";
    private static final String START_MATCH = "La partità può iniziare. \n";
    private static final String CONF_MATCH = "Configurazione ottimale del MatchHandler in corso... \n";

    public StartGame(Table table, Dealer dealer) {
        super(table, dealer);
    }

    @Override
    public void goNext(Context context) {
        Context.logger.info(STATE_STARTED);
        Context.logger.info(CONF_MATCH);
        dealer.setStartChips(table.getSize() * 10000);
        dealer.setInitialBlinds();

        configureRoom();
        sendEventsToPlayers();

        Context.logger.info(START_MATCH);

        context.setState(new StartTurn());
    }

    private void configureRoom() {
        dealer.dealStartingChips();
        table.assignInitialPositions();
        table.sortPlayersByPosition();
    }

    private void sendEventsToPlayers() {
        table.sendBroadcast(new EventsContainer(new RoomCreatedEvent()));
        table.sendBroadcast(preparePlayersLoggedEvents());
    }

    private EventsContainer preparePlayersLoggedEvents() {
        ArrayList<PlayerLoggedEvent> loggedEvents = table.getPlayers()
                .stream()
                .map(player -> new PlayerLoggedEvent(player.getNickname(), player.getAvatar(), player.getPosition(),
                        player.getChips()))
                .collect(Collectors.toCollection(ArrayList::new));
        return new EventsContainer(loggedEvents);
    }

}