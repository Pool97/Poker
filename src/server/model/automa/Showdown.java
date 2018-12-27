package server.model.automa;

import server.algorithm.TurnWinnerEvaluator;
import server.controller.Game;
import server.events.PlayerUpdated;
import server.model.PlayerModel;

import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class Showdown extends AbstractPokerState{

    public Showdown() {

    }

    @Override
    public void goNext(Game game) {
        System.out.println("ShowDown.");
        TurnWinnerEvaluator evaluator = new TurnWinnerEvaluator(table.getPlayers(), dealer.getCommunityModel());

        String nicknameWinner;

        if (table.getPlayersInGame().size() == 1) {
            nicknameWinner = table.getPlayersInGame().stream().findFirst().get().getNickname();
            dealer.givePotTo(nicknameWinner);

        } else {
            evaluator.evaluateTurnWinner();
            game.sendMessage(new server.events.Showdown());
            System.out.println("VINCITORI:");
            evaluator.getWinners().forEach(System.out::println);
            dealer.givePotTo(evaluator.getWinners().get(0));
        }

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ListIterator<PlayerModel> iterator = table.iterator();
        while(iterator.hasNext()){
            PlayerModel player = iterator.next();
            game.sendMessage(
                    new PlayerUpdated(player.getNickname(), player.getChips(), evaluator.getPlayerHandByName(player.getNickname()), 0));
        }



        game.setNextState(new TurnEnd());
    }
}
