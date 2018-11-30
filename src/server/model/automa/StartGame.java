package server.model.automa;

import server.events.EventsContainer;
import server.events.PlayerLoggedEvent;
import server.events.RoomCreatedEvent;
import server.model.Dealer;
import server.model.Table;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StartGame extends AbstractPokerState {
    private static final String STATE_STARTED = "Lo stato di StartGame è avviato.\n";
    private static final String START_MATCH = "La partità può iniziare. \n";
    private static final String CONF_MATCH = "Configurazione ottimale del MatchHandler in corso... \n";

    public StartGame(Table table, Dealer dealer) {
        super(table, dealer);
    }

    @Override
    public void goNext(Game game) {
        Game.logger.info(STATE_STARTED);
        Game.logger.info(CONF_MATCH);
        dealer.setStartChips(table.currentNumberOfPlayers() * 10000);
        dealer.setInitialBlinds();

        configureRoom();

        game.sendMessage(new EventsContainer(new RoomCreatedEvent()));
        game.sendMessage(preparePlayersLoggedEvents());

        Game.logger.info(START_MATCH);

        game.setState(new StartTurn());

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void configureRoom() {
        dealer.dealStartingChips();
        table.assignInitialPositions();
        table.sortPlayersByPosition();
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