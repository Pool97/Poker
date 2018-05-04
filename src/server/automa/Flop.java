package server.automa;

import events.CommunityUpdatedEvent;
import events.Events;
import interfaces.PokerState;
import javafx.util.Pair;
import server.model.CardRank;
import server.model.CardSuit;
import server.model.Room;
import server.model.TurnModel;

/**
 * Flop è lo stato dell'automa che rappresenta l'omonima fase del Poker. In questo stato vengono aggiunte tre carte
 * alle Community Cards, non prima di averne scartata una come da regolamento. Successivamente si invia un messaggio
 * a tutti i players per informarli del cambiamento.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Flop implements PokerState {
    private final static String STATE_STARTED = "Lo stato di Flop è avviato. \n";
    private final static String CARDS_GEN = "Generazione delle tre carte per la Community... \n";
    private final static String CARDS_READY = "Generazione completata. Informo i Players... \n";
    private MatchHandler match;

    public Flop(MatchHandler match) {
        this.match = match;
    }


    /**
     * {@link PokerState}
     */

    @SuppressWarnings("unchecked")
    @Override
    public void goNext() {
        MatchHandler.logger.info(STATE_STARTED);
        TurnModel turnModel = match.getTurnModel();
        Room room = match.getRoom();
        turnModel.getNextCard();

        MatchHandler.logger.info(CARDS_GEN);
        Pair<CardSuit, CardRank> firstCard = turnModel.getNextCard();
        Pair<CardSuit, CardRank> secondCard = turnModel.getNextCard();
        Pair<CardSuit, CardRank> thirdCard = turnModel.getNextCard();

        turnModel.addCommunityCards(firstCard, secondCard, thirdCard);
        MatchHandler.logger.info(CARDS_READY);
        room.sendBroadcast(new Events(new CommunityUpdatedEvent(firstCard, secondCard, thirdCard)));

        Action action = new Action(match);
        action.setTransitionStrategy(() -> match.setState(new Streets(match)));
        match.setState(new Action(match));
    }
}
