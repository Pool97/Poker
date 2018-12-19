package server.model.automa;

import server.controller.Game;
import server.events.PlayerLogged;
import server.events.RoomCreated;
import server.model.Dealer;
import server.model.PlayerModel;
import server.model.Position;
import server.model.Table;

import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

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

        configureRoom();

        game.sendMessage(new RoomCreated());
        preparePlayersLoggedEvents(game);

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
        table.translatePositions();
    }

    private void preparePlayersLoggedEvents(Game game) {
        String position = "";

        ListIterator<PlayerModel> iterator = table.iterator();
        PlayerModel player;
        while(iterator.hasNext()){
            player = iterator.next();
            if(iterator.previousIndex() == Position.SB.ordinal() || iterator.previousIndex() == Position.BB.ordinal())
                position = Position.values()[iterator.previousIndex()].name();
            else if(iterator.previousIndex() == table.size() - 1)
                position = Position.D.name();
            game.sendMessage(new PlayerLogged(player.getNickname(), player.getAvatar(), position,
                    player.getChips()));
            position = "";
        }
    }

}