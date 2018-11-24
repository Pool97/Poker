package server.model.automa;

import server.events.BlindsUpdatedEvent;
import server.events.EventsContainer;
import server.events.PlayerHasWinEvent;
import server.events.TurnStartedEvent;
import server.model.GameType;
import server.model.PlayerModel;
import server.model.cards.CardModel;

import java.util.ArrayList;

public class StartTurn extends AbstractPokerState {
    private final static String STATE_STARTED = "Lo stato di StartTurn è avviato. \n";
    private final static String CONF_TURN = "Configurazione dei parametri del turno in corso... \n";
    private GameType gameType;


    public StartTurn() {
        super();
        gameType = dealer.getGameType();
    }

    @Override
    public void goNext(Context context) {
        Context.logger.info(STATE_STARTED);
        Context.logger.info(CONF_TURN);

        if (table.hasWinner()) {
            table.sendBroadcast(new EventsContainer(new PlayerHasWinEvent(table.getWinner())));
            table.sendBroadcastToLostPlayers(new EventsContainer(new PlayerHasWinEvent(table.getWinner())));
            context.setState(new RestartMatch());
        } else {
            updateMatchParameters();
            updateRoom();
            dealer.dealCards();
            sendNewBlindsToPlayers();

            table.sendBroadcast(prepareTurnStartedEvents());

            context.setState(new SmallBlind());
        }
    }

    private void updateRoom() {
        table.removeDisconnectedPlayers();
        table.movePlayersPosition();
    }

    private void updateMatchParameters() {
        dealer.shuffleCards();
        table.resetPot();
        table.emptyCommunity();
        gameType.increaseBlinds();
    }

    private void sendNewBlindsToPlayers() {
        table.sendBroadcast(new EventsContainer(new BlindsUpdatedEvent(gameType.getSmallBlind(), gameType.getBigBlind())));
    }

    private EventsContainer prepareTurnStartedEvents() {
        ArrayList<TurnStartedEvent> events = new ArrayList<>();
        for (PlayerModel player : table.getPlayers()) {
            TurnStartedEvent event = new TurnStartedEvent(player.getNickname(), player.getPosition().name());
            player.cardsStream().map(CardModel::getImageDirectoryPath).forEach(event::addCardPath);
            events.add(event);
        }
        return new EventsContainer(events);
    }
}