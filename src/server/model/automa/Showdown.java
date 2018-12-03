package server.model.automa;

import server.events.EventsContainer;
import server.events.PlayerUpdatedEvent;
import server.events.ShowdownEvent;
import server.model.PlayerModel;
import server.model.TurnWinnerEvaluator;

import java.util.ListIterator;

public class Showdown extends AbstractPokerState{

    public Showdown() {
    }

    @Override
    public void goNext(Game game) {
        System.out.println("ShowDown.");
        EventsContainer eventsContainer = new EventsContainer();
        TurnWinnerEvaluator evaluator = new TurnWinnerEvaluator(table.getPlayers(), dealer.getCommunityModel());

        String nicknameWinner;

        if (table.getPlayersInGame().size() == 1) {
            nicknameWinner = table.getPlayersInGame().stream().findFirst().get().getNickname();
            dealer.givePotTo(nicknameWinner);

        } else {
            evaluator.evaluateTurnWinner();
            eventsContainer.addEvent(new ShowdownEvent());
            System.out.println("VINCITORI:");
            evaluator.getWinners().forEach(System.out::println);
            dealer.givePotTo(evaluator.getWinners().get(0));
        }

        ListIterator<PlayerModel> iterator = table.iterator();
        while(iterator.hasNext()){
            PlayerModel player = iterator.next();
            eventsContainer.addEvent(
                    new PlayerUpdatedEvent(player.getNickname(), player.getChips(), evaluator.getPlayerHandByName(player.getNickname())));
        }

        game.sendMessage(eventsContainer);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        game.setState(new TurnEnd());
    }
}
