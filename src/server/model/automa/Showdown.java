package server.model.automa;

import server.model.PlayersStateAnalyzer;
import server.model.TurnWinnerEvaluator;
import server.events.EventsContainer;
import server.events.PlayerUpdatedEvent;
import server.events.ShowdownEvent;

public class Showdown extends AbstractPokerState{

    public Showdown() {
    }

    @Override
    public void goNext(Game game) {
        System.out.println("ShowDown.");
        EventsContainer eventsContainer = new EventsContainer();
        TurnWinnerEvaluator evaluator = new TurnWinnerEvaluator(table.getPlayers(), dealer.getCommunityModel());

        String nicknameWinner;

        PlayersStateAnalyzer analyzer = new PlayersStateAnalyzer(table.getPlayers());
        if (analyzer.getPlayersAtStake().size() == 1) {
            nicknameWinner = analyzer.getPlayersAtStake().stream().findFirst().get().getNickname();
        } else {
            nicknameWinner = evaluator.evaluateTurnWinner();
            eventsContainer.addEvent(new ShowdownEvent());
        }

        dealer.givePotTo(nicknameWinner);

        table.getPlayers().forEach(player -> eventsContainer.addEvent(new PlayerUpdatedEvent(player.getNickname(), player.getChips(), evaluator.getPlayerHandByName(player.getNickname()))));

        game.sendMessage(eventsContainer);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        game.setState(new TurnEnd());
    }
}
