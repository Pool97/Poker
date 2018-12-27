package server.model.automa;

import server.controller.Game;
import server.model.Position;
import server.model.automa.round.LimitRound;
import server.model.automa.round.NoLimitRound;
import server.model.gamestructure.FixedLimit;
import server.model.gamestructure.NoLimit;

public class Turn extends Street{

    @Override
    public void goNext(Game game) {
        super.goNext(game);
    }

    @Override
    public void nextState(NoLimit type) {
        nextState = new NoLimitRound(Position.SB.ordinal());
        nextState.setTransitionStrategy(River::new);
    }

    @Override
    public void nextState(FixedLimit type) {
        nextState = new LimitRound(type.getHigherLimit(), Position.SB.ordinal());
        nextState.setTransitionStrategy(River::new);
    }
}
