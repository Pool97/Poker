package server.model.automa;

import interfaces.PokerState;
import server.events.CommunityUpdatedEvent;
import server.events.EventsContainer;

public class Turn extends Street implements PokerState {

    @Override
    public void goNext(Game game) {
        dealer.burnCard();

        game.sendMessage(new EventsContainer(new CommunityUpdatedEvent(dealer.dealCommunityCard())));
        NextNoLimitRound nextAction = new NextNoLimitRound();
        nextAction.setTransitionStrategy(() -> game.setState(new River()));
        game.setState(nextAction);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
