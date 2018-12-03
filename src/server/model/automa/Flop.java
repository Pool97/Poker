package server.model.automa;

import server.events.CommunityUpdatedEvent;
import server.events.EventsContainer;

public class Flop extends AbstractPokerState {
    private final static String STATE_STARTED = "Lo stato di Flop è avviato. \n";
    private final static String CARDS_GEN = "Generazione delle tre carte per la Community... \n";
    private final static String CARDS_READY = "Generazione completata. Informo i Players... \n";

    @Override
    public void goNext(Game game) {
        Game.logger.info(STATE_STARTED);
        dealer.burnCard();

        Game.logger.info(CARDS_GEN);

        Game.logger.info(CARDS_READY);
        game.sendMessage(new EventsContainer(new CommunityUpdatedEvent(dealer.dealCommunityCard(),
                dealer.dealCommunityCard(), dealer.dealCommunityCard())));

        NextBettingRound nextAction = new NextBettingRound();
        nextAction.setTransitionStrategy(() -> game.setState(new Turn()));
        game.setState(nextAction);
    }
}
