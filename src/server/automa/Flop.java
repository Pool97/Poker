package server.automa;

import interfaces.PokerState;
import server.controller.MatchHandler;
import server.controller.Room;
import server.events.CommunityUpdatedEvent;
import server.events.EventsContainer;
import server.model.TurnModel;
import server.model.cards.CardModel;

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

        CardModel firstCard = turnModel.getNextCard();
        CardModel secondCard = turnModel.getNextCard();
        CardModel thirdCard = turnModel.getNextCard();

        turnModel.addCommunityCards(firstCard, secondCard, thirdCard);

        MatchHandler.logger.info(CARDS_READY);
        room.sendBroadcast(new EventsContainer(new CommunityUpdatedEvent(firstCard, secondCard, thirdCard)));

        NextBettingRound nextAction = new NextBettingRound(match);
        nextAction.setTransitionStrategy(() -> match.setState(new Turn(match)));
        match.setState(nextAction);
    }
}
