package server.model.automa;

import server.controller.Game;
import server.events.*;
import server.model.PlayerModel;

import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class TurnEnd extends AbstractPokerState{

    public TurnEnd() {

    }

    @Override
    public void goNext(Game game) {
        System.out.println("TurnEnd.");

        game.sendMessage(new PotUpdated(dealer.getPotValue()));

        ListIterator<PlayerModel> iterator = table.iterator();
        PlayerModel player;
        while(iterator.hasNext()){
            player = iterator.next();
            player.giveBackCards();
            if(player.getChips() <= 0) {
                player.setLost(true);
                game.sendMessage(new MatchLost(player.getNickname(), table.currentNumberOfPlayers(), player.isCreator()));
            }
            player.setFolded(false);
            player.setAllIn(false);
        }

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        game.sendMessage(new TurnEnded());

        dealer.emptyCommunity();
        table.refreshLists();
        if (table.hasWinner()) {
            game.sendMessage(new PlayerHasWin(table.getWinner()));
            game.sendMessage(new ServerClosed());
            game.stop();
        }else
            game.setNextState(new StartTurn());
    }

}
