package server.automa;

import server.controller.Context;
import server.controller.Room;
import server.model.MatchModel;
import server.model.PlayerModel;
import server.model.Position;
import server.model.actions.DeadMoney;

public class SmallBlind extends Blind {
    private final static String SMALL_BLIND = "Riscuoto la puntata obbligatoria di Small Blind... \n";

    public SmallBlind(Context match) {
        super(match);
    }

    @Override
    public void goNext() {
        MatchModel matchModel = match.getMatchModel();

        Room room = match.getRoom();
        PlayerModel playerModel;

        Context.logger.info(SMALL_BLIND);

        playerModel = room.getPlayer(Position.SB);
        if (playerModel.getChips() < matchModel.getSmallBlind()) {
            int payed = playerModel.getChips();
            int deltaBlind = matchModel.getSmallBlind() - payed;
            payBlindAndUpdate(playerModel, payed);
            playerModel.addAction(new DeadMoney(deltaBlind));
            increasePotAndUpdate(payed);
        } else {
            payBlindAndUpdate(playerModel, matchModel.getSmallBlind());
            increasePotAndUpdate(matchModel.getSmallBlind());
        }

        match.setState(new BigBlind(match));
    }
}
