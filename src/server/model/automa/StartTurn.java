package server.model.automa;

import server.events.BlindsUpdatedEvent;
import server.events.EventsContainer;
import server.events.PlayerHasWinEvent;
import server.events.TurnStartedEvent;
import server.model.PlayerModel;
import server.model.cards.CardModel;

import java.util.ArrayList;

public class StartTurn extends AbstractPokerState {
    private final static String STATE_STARTED = "Lo stato di StartTurn è avviato. \n";
    private final static String CONF_TURN = "Configurazione dei parametri del turno in corso... \n";

    public StartTurn() {
        super();
    }

    @Override
    public void goNext(Game game) {
        Game.logger.info(STATE_STARTED);
        Game.logger.info(CONF_TURN);

        if (table.hasWinner()) {

            game.sendMessage(new EventsContainer(new PlayerHasWinEvent(table.getWinner())));
            game.setState(new RestartMatch());

        } else {

            dealer.shuffleCards();
            dealer.increaseBlinds();
            game.sendMessage(new EventsContainer(new BlindsUpdatedEvent(dealer.getSmallBlind(), dealer.getBigBlind())));

            table.removeDisconnectedPlayers();
            table.movePlayersPosition();

            table.getPlayers()
                    .forEach(playerModel -> playerModel.receiveCards(dealer.dealCard(), dealer.dealCard()));

            game.sendMessage(prepareTurnStartedEvents());

            game.setState(new ForcedBets());
        }
    }

    private EventsContainer prepareTurnStartedEvents() {
        ArrayList<TurnStartedEvent> events = new ArrayList<>();
        for (PlayerModel player : table.getPlayers()) {
            TurnStartedEvent event = new TurnStartedEvent(player.getNickname(), player.getPosition().name());
            player.getCards().stream().map(CardModel::getImageDirectoryPath).forEach(event::addCardPath);
            events.add(event);
        }
        return new EventsContainer(events);
    }
}