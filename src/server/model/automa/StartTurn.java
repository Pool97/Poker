package server.model.automa;

import server.controller.Game;
import server.events.ChatNotify;
import server.events.PlayerHasWin;
import server.events.ServerClosed;
import server.events.TurnStarted;
import server.model.PlayerModel;
import server.model.Position;
import server.model.cards.CardModel;

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
            game.sendMessage(new PlayerHasWin(table.getWinner()));
            game.sendMessage(new ServerClosed());
            game.stop();
        } else {

            dealer.shuffleCards();
            game.increaseBlinds();
            game.sendMessage(new ChatNotify("Blinds aumentati. Small blind: $"  + game.getSmallBlind()
            + ", big blind: $" + game.getBigBlind()));

            table.removeDisconnectedPlayers();
            table.translatePositions();

            for(PlayerModel player : table) player.receiveCards(dealer.dealCard(), dealer.dealCard());
            prepareTurnStartedEvents(game);

            game.setState(new ForcedBets());
        }
    }

    private void prepareTurnStartedEvents(Game game) {
        String position = "";

        ListIterator<PlayerModel> iterator = table.iterator();
        PlayerModel player;
        while(iterator.hasNext()){
            player = iterator.next();

            if(iterator.previousIndex() == 0 || iterator.previousIndex() == 1)
                position = Position.values()[iterator.previousIndex()].name();
            else if(iterator.previousIndex() == table.size() - 1)
                position = Position.D.name();

            TurnStarted event = new TurnStarted(player.getNickname(), position);
            player.getCards().stream().map(CardModel::getImageDirectoryPath).forEach(event::addCardPath);
            game.sendMessage(event);
            position = "";
        }
    }
}