package server.model.automa;

import interfaces.PokerState;
import server.events.CommunityUpdatedEvent;
import server.events.EventsContainer;

public class Turn extends Street implements PokerState {

    public Turn() {
        super();
        showNextCard();
        table.sendBroadcast(new EventsContainer(new CommunityUpdatedEvent(table.getCommunityModel().getCard(3))));
    }

    @Override
    public void goNext(Context context) {
        NextBettingRound nextAction = new NextBettingRound();
        nextAction.setTransitionStrategy(() -> context.setState(new River()));
        context.setState(nextAction);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
