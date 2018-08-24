package server.automa;

import server.model.*;

public class SmallBlind extends Blind {
    private final static String SMALL_BLIND = "Riscuoto la puntata obbligatoria di Small Blind... \n";

    public SmallBlind(MatchHandler match) {
        super(match);
    }

    @Override
    public void goNext() {
        MatchModel matchModel = match.getMatchModel();

        Room room = match.getRoom();
        PlayerModel playerModel;

        MatchHandler.logger.info(SMALL_BLIND);

        playerModel = room.getPlayer(Position.SB);
        if (playerModel.getChips() < matchModel.getSmallBlind()) {
            int payed = playerModel.getChips();
            int deltaBlind = matchModel.getSmallBlind() - payed;
            payBlindAndUpdate(playerModel, payed);
            playerModel.addAction(new Fittizia(deltaBlind));
            increasePotAndUpdate(payed);
        } else {
            payBlindAndUpdate(playerModel, matchModel.getSmallBlind());
            increasePotAndUpdate(matchModel.getSmallBlind());
        }


        match.setState(new BigBlind(match));
    }
}
