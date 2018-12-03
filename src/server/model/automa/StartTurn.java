package server.model.automa;

import server.events.BlindsUpdatedEvent;
import server.events.EventsContainer;
import server.events.PlayerHasWinEvent;
import server.events.TurnStartedEvent;
import server.model.PlayerModel;
import server.model.Position;
import server.model.cards.CardModel;

import java.util.ArrayList;
import java.util.ListIterator;

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
            game.increaseBlinds();
            game.sendMessage(new EventsContainer(new BlindsUpdatedEvent(game.getSmallBlind(), game.getBigBlind())));

            table.removeDisconnectedPlayers();
            table.translatePositions();

            for(PlayerModel player : table) player.receiveCards(dealer.dealCard(), dealer.dealCard());

            game.sendMessage(prepareTurnStartedEvents());

            game.setState(new ForcedBets());
        }
    }

    private EventsContainer prepareTurnStartedEvents() {
        ArrayList<TurnStartedEvent> events = new ArrayList<>();
        String position = "";

        ListIterator<PlayerModel> iterator = table.iterator();
        PlayerModel player;
        while(iterator.hasNext()){
            player = iterator.next();
            if(iterator.previousIndex() == 0 || iterator.previousIndex() == 1 || iterator.previousIndex() == table.size() - 1)
                position = Position.values()[iterator.previousIndex()].name();

            TurnStartedEvent event = new TurnStartedEvent(player.getNickname(), position);
            player.getCards().stream().map(CardModel::getImageDirectoryPath).forEach(event::addCardPath);
            events.add(event);
            position = "";
        }

        return new EventsContainer(events);
    }
}