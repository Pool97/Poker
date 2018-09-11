package server.automa;

import server.controller.MatchHandler;
import server.model.MatchModel;
import server.model.PlayerModel;
import server.model.Position;
import server.model.actions.DeadMoney;

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

        int payedAmount;

        if (playerModel.getChips() < matchModel.getBigBlind()) {
            int available = playerModel.getChips();
            int notPayed = matchModel.getBigBlind() - available;
            payedAmount = available;
            playerModel.addAction(new DeadMoney(notPayed));
        } else {
            payedAmount = matchModel.getBigBlind();
        }

        payBlindAndUpdate(playerModel, payedAmount);
        increasePotAndUpdate(payedAmount);

        match.setState(new FirstBettingRound(match));
    }
}
