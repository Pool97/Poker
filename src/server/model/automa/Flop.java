package server.model.automa;

import server.controller.Game;
import server.events.CommunityUpdated;
import server.model.Position;
import server.model.automa.round.LimitRound;
import server.model.automa.round.NoLimitRound;
import server.model.gamestructure.FixedLimit;
import server.model.gamestructure.NoLimit;

public class Flop extends AbstractPokerState {
    private final static String STATE_STARTED = "Lo stato di Flop è avviato. \n";
    private final static String CARDS_GEN = "Generazione delle tre carte per la Community... \n";
    private final static String CARDS_READY = "Generazione completata. Informo i Players... \n";
    private AbstractPokerState nextState;

    @Override
    public void goNext(Game game) {
        Game.logger.info(STATE_STARTED);
        dealer.burnCard();
        Game.logger.info(CARDS_GEN);

        Game.logger.info(CARDS_READY);

        game.sendMessage(new CommunityUpdated(dealer.dealCommunityCard(),
                dealer.dealCommunityCard(), dealer.dealCommunityCard()));

        game.getBettingStructure().reach(this);

        game.setNextState(nextState);
    }

    @Override
    public void nextState(NoLimit type) {
        nextState = new NoLimitRound(Position.SB.ordinal());
        nextState.setTransitionStrategy(Turn::new);
    }

    @Override
    public void nextState(FixedLimit type) {
        nextState = new LimitRound(type.getLowerLimit(), Position.SB.ordinal());
        nextState.setTransitionStrategy(Turn::new);
    }
}
