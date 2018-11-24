package server.model.automa;

import server.model.GameType;
import server.model.PlayerModel;
import server.model.Position;
import server.model.actions.DeadMoney;

public class SmallBlind extends Blind {
    private final static String SMALL_BLIND = "Riscuoto la puntata obbligatoria di Small Blind... \n";

    public SmallBlind() {
        super();
    }

    @Override
    public void goNext(Context context) {
        GameType gameType = dealer.getGameType();

        PlayerModel playerModel;

        Context.logger.info(SMALL_BLIND);

        playerModel = table.getPlayer(Position.SB);
        if (playerModel.getChips() < gameType.getSmallBlind()) {
            int payed = playerModel.getChips();
            int deltaBlind = gameType.getSmallBlind() - payed;
            payBlindAndUpdate(playerModel, payed);
            playerModel.addAction(new DeadMoney(deltaBlind));
            increasePotAndUpdate(payed);
        } else {
            payBlindAndUpdate(playerModel, gameType.getSmallBlind());
            increasePotAndUpdate(gameType.getSmallBlind());
        }

        context.setState(new BigBlind());
    }
}
