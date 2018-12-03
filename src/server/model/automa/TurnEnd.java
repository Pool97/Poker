package server.model.automa;

import server.events.EventsContainer;
import server.events.MatchLostEvent;
import server.events.PotUpdatedEvent;
import server.events.TurnEndedEvent;
import server.model.PlayerModel;

import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class TurnEnd extends AbstractPokerState{

    public TurnEnd() {
    }

    @Override
    public void goNext(Game game) {
        System.out.println("TurnEnd.");

        EventsContainer eventsContainer = new EventsContainer();
        eventsContainer.addEvent(new PotUpdatedEvent(dealer.getPotValue()));

        ListIterator<PlayerModel> iterator = table.iterator();
        PlayerModel player;
        while(iterator.hasNext()){
            player = iterator.next();
            player.giveBackCards();
            if(player.getChips() <= 0) {
                player.setLost(true);
                eventsContainer.addEvent(new MatchLostEvent(player.getNickname(), table.currentNumberOfPlayers() + 1, player.isCreator()));
            }
            player.setFolded(false);
        }

        eventsContainer.addEvent(new TurnEndedEvent());

        game.sendMessage(eventsContainer);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dealer.emptyCommunity();
        table.refreshLists();
        game.setState(new StartTurn());
    }

}
