package server.automa;

import interfaces.PokerState;
import server.controller.*;
import server.events.EventsContainer;
import server.events.PlayerUpdatedEvent;
import server.events.ShowdownEvent;
import server.model.PlayerModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Showdown implements PokerState {
    private MatchHandler match;
    private Room room;

    public Showdown(MatchHandler match) {
        this.match = match;
        this.room = match.getRoom();
    }

    @Override
    public void goNext() {
        System.out.println("ShowDown.");
        EventsContainer eventsContainer = new EventsContainer();

        TurnWinnerEvaluator evaluator = new TurnWinnerEvaluator(room.getPlayers(), match.getTurnModel().getCommunityModel());
        PotHandler pot = new PotHandler(room.getPlayers());

        String nicknameWinner;

        PlayersStateAnalyzer analyzer = new PlayersStateAnalyzer(room.getControllers());
        if (analyzer.getPlayersAtStake().size() == 1) {
            nicknameWinner = analyzer.getPlayersAtStake().stream().findFirst().get().getNickname();
        } else {
            ArrayList<PlayerModel> inGamePlayers = room.getPlayers().stream()
                    .filter(playerModel -> !playerModel.hasFolded())
                    .collect(Collectors.toCollection(ArrayList::new));
            for(PlayerModel player : inGamePlayers){
                evaluator.checkWhoWin(match.getTurnModel().getCommunityModel().getCommunityCards(), player.getCards().get(0), player.getCards().get(1));
            }
            evaluator.letsStart();
            //nicknameWinner = evaluator.evaluateTurnWinner();
            nicknameWinner = evaluator.getWinners().get(0);
            eventsContainer.addEvent(new ShowdownEvent());
        }

        pot.assignPotsTo(nicknameWinner);

        room.getPlayers().forEach(player -> eventsContainer.addEvent(new PlayerUpdatedEvent(player.getNickname(), player.getChips(), evaluator.getPlayerHandByName(player.getNickname()))));

        room.sendBroadcast(eventsContainer);
        room.sendBroadcastToLostPlayers(eventsContainer);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        match.setState(new TurnEnd(match));
    }
}
