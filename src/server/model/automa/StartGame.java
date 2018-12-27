package server.model.automa;

import server.controller.Game;
import server.events.PlayerLogged;
import server.events.RoomCreated;
import server.model.Dealer;
import server.model.PlayerModel;
import server.model.Table;

import java.util.concurrent.TimeUnit;

public class StartGame extends AbstractPokerState {
    private static final String START_MATCH = "La partità può iniziare. \n";

    public StartGame(Table table, Dealer dealer) {
        super(table, dealer);
        strategy = StartTurn::new;
    }

    @Override
    public void goNext(Game game) {
        dealer.dealStartingChips();

        game.sendMessage(new RoomCreated());

        for(PlayerModel player : table)
            game.sendMessage(new PlayerLogged(player.getNickname(), player.getAvatar(), player.getChips()));

        Game.logger.info(START_MATCH);

        game.setNextState(strategy.determineTransition());

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}