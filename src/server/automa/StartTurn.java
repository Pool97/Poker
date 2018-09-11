package server.automa;

import interfaces.PokerState;
import server.controller.MatchHandler;
import server.controller.Room;
import server.events.BlindsUpdatedEvent;
import server.events.EventsContainer;
import server.events.PlayerHasWinEvent;
import server.events.TurnStartedEvent;
import server.model.MatchModel;
import server.model.PlayerModel;
import server.model.TurnModel;
import server.model.cards.CardModel;

import java.util.ArrayList;

public class StartTurn implements PokerState {
    private final static String STATE_STARTED = "Lo stato di StartTurn è avviato. \n";
    private final static String CONF_TURN = "Configurazione dei parametri del turno in corso... \n";

    private MatchHandler match;
    private TurnModel turnModel;
    private MatchModel matchModel;
    private Room room;


    public StartTurn(MatchHandler match) {
        this.match = match;
        turnModel = match.getTurnModel();
        matchModel = match.getMatchModel();
        room = match.getRoom();
    }

    @Override
    public void goNext() {
        MatchHandler.logger.info(STATE_STARTED);
        MatchHandler.logger.info(CONF_TURN);

        if (room.hasWinner()) {
            room.sendBroadcast(new EventsContainer(new PlayerHasWinEvent(room.getWinner())));
            room.sendBroadcastToLostPlayers(new EventsContainer(new PlayerHasWinEvent(room.getWinner())));
            match.setState(new RestartMatch(match));
        } else {
            updateMatchParameters();
            updateRoom();
            dealCards();
            sendNewBlindsToPlayers();

            room.sendBroadcast(prepareTurnStartedEvents());

            match.setState(new SmallBlind(match));
        }
    }

    private void updateRoom() {
        room.removeDisconnectedPlayers();
        room.movePlayersPosition();
        //room.getPlayers().forEach(playerModel -> System.out.println(playerModel.getNickname() + " " + playerModel.getPosition().name()));
    }

    private void updateMatchParameters() {
        turnModel.createDeck();
        turnModel.resetPot();
        turnModel.emptyCommunity();
        matchModel.increaseBlinds();

    }

    private void sendNewBlindsToPlayers() {
        room.sendBroadcast(new EventsContainer(new BlindsUpdatedEvent(matchModel.getSmallBlind(), matchModel.getBigBlind())));
    }

    private EventsContainer prepareTurnStartedEvents() {
        ArrayList<TurnStartedEvent> events = new ArrayList<>();
        for (PlayerModel player : room.getPlayers()) {
            TurnStartedEvent event = new TurnStartedEvent(player.getNickname(), player.getPosition().name());
            player.cardsStream().map(CardModel::getImageDirectoryPath).forEach(event::addCardPath);
            events.add(event);
        }
        return new EventsContainer(events);
    }

    private void dealCards() {
        room.getPlayers().forEach(player -> player.addCard(turnModel.getNextCard()));
        room.getPlayers().forEach(player -> player.addCard(turnModel.getNextCard()));
    }
}