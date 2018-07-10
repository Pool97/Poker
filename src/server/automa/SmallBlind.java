package server.automa;

import server.model.MatchModel;
import server.model.PlayerModel;
import server.model.Position;
import server.model.Room;

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
        payBlindAndUpdate(playerModel, matchModel.getSmallBlind());
        increasePotAndUpdate(matchModel.getSmallBlind());

        match.setState(new BigBlind(match));
    }
}
