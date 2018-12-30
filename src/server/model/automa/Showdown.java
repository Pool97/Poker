package server.model.automa;

import server.algorithm.TurnWinnerEvaluator;
import server.controller.Game;
import server.events.PlayerUpdated;
import server.model.PlayerModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Showdown extends AbstractPokerState{

    public Showdown() {
        strategy = TurnEnd::new;
    }

    @Override
    public void goNext(Game game) {
        System.out.println("ShowDown.");
        algo();

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TurnWinnerEvaluator evaluator = new TurnWinnerEvaluator(table.getPlayersInGame(), dealer.getCommunityModel());
        evaluator.evaluateTurnWinner();

        server.events.Showdown event = new server.events.Showdown();

        if(table.countPlayersInGame() > 1) {
            table.getPlayersInGame().forEach(player -> event.addNicknamePlayerToList(player.getNickname()));
            game.sendMessage(event);
            table.getPlayersInGame().forEach(player ->
                    game.sendMessage(new PlayerUpdated(player.getNickname(), player.getChips(),
                            evaluator.getPlayerHandByName(player.getNickname()), 0)));
        }else if(table.countPlayersInGame() == 1){
            table.getPlayersInGame().forEach(player ->
                    game.sendMessage(new PlayerUpdated(player.getNickname(), player.getChips(), "", 0)));
        }

        game.setNextState(strategy.determineTransition());
    }

    public void algo(){

        if (table.getPlayersInGame().size() == 1) {
            String nicknameWinner = table.getPlayersInGame().stream().findFirst().get().getNickname();
            table.getPlayerByName(nicknameWinner).addChips(dealer.getPotValue());
            dealer.emptyPot();
        }else{
            for (int i = 0; i < dealer.totalSidePots(); i++) {
                ArrayList<String> playersInSidePot = dealer.playersInPot(i);
                ArrayList<PlayerModel> playerModels = playersInSidePot.stream()
                        .map(nickname -> table.getPlayerByName(nickname))
                        .collect(Collectors.toCollection(ArrayList::new));

                TurnWinnerEvaluator evaluator = new TurnWinnerEvaluator(playerModels, dealer.getCommunityModel());

                if (evaluator.evaluateTurnWinner()) {
                    int numberOfWinners = evaluator.getWinners().size();
                    int potWin = dealer.getSidePotValue(i) / numberOfWinners;
                    evaluator.getWinners().stream().map(nickname -> table.getPlayerByName(nickname)).forEach(player -> player.addChips(potWin));
                } else {
                    table.getPlayerByName(evaluator.getWinners().get(0)).addChips(dealer.getSidePotValue(i));
                }
            }
            dealer.emptyPot();
        }
    }
}
