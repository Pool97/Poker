package server.automa;

import server.model.Fittizia;
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

        if (playerModel.getChips() < matchModel.getBigBlind()) {
            int payed = playerModel.getChips();
            int deltaBlind = matchModel.getBigBlind() - payed;
            payBlindAndUpdate(playerModel, payed);
            playerModel.addAction(new Fittizia(deltaBlind));
            increasePotAndUpdate(payed);
        } else {
            payBlindAndUpdate(playerModel, matchModel.getBigBlind());
            increasePotAndUpdate(matchModel.getBigBlind());
        }

        match.setState(new FirstBettingRound(match));
    }
}
