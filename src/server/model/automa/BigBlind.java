package server.model.automa;

import server.model.GameType;
import server.model.PlayerModel;
import server.model.Position;
import server.model.actions.DeadMoney;

public class BigBlind extends Blind {
    private final static String BIG_BLIND = "Riscuoto la puntata obligatoria di Big Blind \n";

    public BigBlind() {
        super();
    }

    @Override
    public void goNext(Context context) {
        Context.logger.info(BIG_BLIND);
        GameType gameType = dealer.getGameType();
        PlayerModel playerModel = table.getPlayer(Position.BB);

        int payedAmount;

        if (playerModel.getChips() < gameType.getBigBlind()) {
            int available = playerModel.getChips();
            int notPayed = gameType.getBigBlind() - available;
            payedAmount = available;
            playerModel.addAction(new DeadMoney(notPayed));
        } else {
            payedAmount = gameType.getBigBlind();
        }

        payBlindAndUpdate(playerModel, payedAmount);
        increasePotAndUpdate(payedAmount);

        context.setState(new FirstBettingRound());
    }
}
