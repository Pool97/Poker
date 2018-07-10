package server.automa;

import server.model.MatchModel;
import server.model.PlayerModel;
import server.model.Position;

public class BigBlind extends Blind {
    private final static String BIG_BLIND = "Riscuoto la puntata obligatoria di Big Blind \n";

    public BigBlind(MatchHandler match) {
        super(match);
    }

    @Override
    public void goNext() {
        MatchHandler.logger.info(BIG_BLIND);
        MatchModel matchModel = match.getMatchModel();
        PlayerModel playerModel = match.getRoom().getPlayer(Position.BB);

        payBlindAndUpdate(playerModel, matchModel.getBigBlind());
        increasePotAndUpdate(matchModel.getBigBlind());

        match.setState(new FirstAction(match));
    }
}
