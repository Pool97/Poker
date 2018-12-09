package server.model.automa;

import server.events.CommunityUpdatedEvent;
import server.events.EventsContainer;
import server.model.automa.round.FirstLimitRound;
import server.model.automa.round.NextNoLimitRound;
import server.model.gamestructure.FixedLimit;
import server.model.gamestructure.NoLimit;

public class Flop extends AbstractPokerState {
    private final static String STATE_STARTED = "Lo stato di Flop è avviato. \n";
    private final static String CARDS_GEN = "Generazione delle tre carte per la Community... \n";
    private final static String CARDS_READY = "Generazione completata. Informo i Players... \n";
    private AbstractPokerState nextState;
    private int fixedLimit;

    @Override
    public void goNext(Game game) {
        Game.logger.info(STATE_STARTED);
        dealer.burnCard();
        fixedLimit = game.getBigBlind();
        Game.logger.info(CARDS_GEN);

        Game.logger.info(CARDS_READY);
        game.sendMessage(new EventsContainer(new CommunityUpdatedEvent(dealer.dealCommunityCard(),
                dealer.dealCommunityCard(), dealer.dealCommunityCard())));

        game.getBettingStructure().reach(this);

        game.setState(nextState);
    }

    @Override
    public void nextState(NoLimit type) {
        nextState = new NextNoLimitRound();
        ((NextNoLimitRound) nextState).setTransitionStrategy(Turn::new);
    }

    @Override
    public void nextState(FixedLimit type) {
        nextState = new FirstLimitRound(fixedLimit);
        ((FirstLimitRound) nextState).setTransitionStrategy(Turn::new);
    }
}
