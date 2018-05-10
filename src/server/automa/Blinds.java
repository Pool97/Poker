package server.automa;

import events.Events;
import events.PlayerUpdatedEvent;
import events.PotUpdatedEvent;
import interfaces.PokerState;
import javafx.util.Pair;
import server.model.*;

/**
 * Questo è lo stato dell'automa che si occupa di riscuotere le puntate obbligatorie del turno, ossia lo Small Blind e
 * il Big Blind.
 * Informa successivamente a tutti i Players delle modifiche apportate alle chips dei Player in SB e BB e delle
 * modifiche apportate al valore del pot.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Blinds implements PokerState {
    private final static String STATE_STARTED = "Lo stato di Blinds è avviato \n";
    private final static String SMALL_BLIND = "Riscuoto la puntata obbligatoria di Small Blind... \n";
    private final static String BIG_BLIND = "Riscuoto la puntata obligatoria di Big Blind \n";
    private MatchHandler match;

    /**
     * Costruttore della classe Blinds.
     *
     * @param match Il gestore dell'automa.
     */

    public Blinds(MatchHandler match) {
        this.match = match;
    }

    /**
     * {@link PokerState}
     */

    @Override
    public void goNext() {
        MatchHandler.logger.info(STATE_STARTED);
        MatchModel matchModel = match.getMatchModel();
        TurnModel turnModel = match.getTurnModel();
        turnModel.resetPot();
        Room room = match.getRoom();
        PlayerModel playerModel;

        MatchHandler.logger.info(SMALL_BLIND);
        playerModel = room.getPlayer(Position.SB);
        playerModel.addAction(new Pair<>(ActionType.SB, matchModel.getSmallBlind()));
        int potIncreased = turnModel.increasePot(matchModel.getSmallBlind());
        System.out.println("FIRST SB: " + matchModel.getSmallBlind());
        System.out.println("FIRST BB: " + matchModel.getBigBlind());
        System.out.println("FIRST POT" + turnModel.getPot());
        room.sendBroadcast(new Events(new PlayerUpdatedEvent(playerModel), new PotUpdatedEvent(potIncreased)));

        MatchHandler.logger.info(BIG_BLIND);
        playerModel = room.getPlayer(Position.BB);
        playerModel.addAction(new Pair<>(ActionType.BB, matchModel.getBigBlind()));
        potIncreased = turnModel.increasePot(matchModel.getBigBlind());
        System.out.println("SECOND POT" + turnModel.getPot());
        room.sendBroadcast(new Events(new PlayerUpdatedEvent(playerModel), new PotUpdatedEvent(potIncreased)));

        Action action = new Action(match);
        action.setTransitionStrategy(() -> match.setState(new Flop(match)));
        match.setState(action);
    }
}
