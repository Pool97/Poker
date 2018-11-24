package server.model.automa;

import interfaces.PokerState;
import server.events.CommunityUpdatedEvent;
import server.events.EventsContainer;

/**
 * Flop è lo stato dell'automa che rappresenta l'omonima fase del Poker. In questo stato vengono aggiunte tre carte
 * alle Community Cards, non prima di averne scartata una come da regolamento. Successivamente si invia un messaggio
 * a tutti i players per informarli del cambiamento.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Flop extends AbstractPokerState {
    private final static String STATE_STARTED = "Lo stato di Flop è avviato. \n";
    private final static String CARDS_GEN = "Generazione delle tre carte per la Community... \n";
    private final static String CARDS_READY = "Generazione completata. Informo i Players... \n";

    public Flop() {
    }


    /**
     * {@link PokerState}
     */

    @SuppressWarnings("unchecked")
    @Override
    public void goNext(Context context) {
        Context.logger.info(STATE_STARTED);
        dealer.getNextCard();

        Context.logger.info(CARDS_GEN);

        dealer.dealCommunityCard();
        dealer.dealCommunityCard();
        dealer.dealCommunityCard();

        Context.logger.info(CARDS_READY);
        table.sendBroadcast(new EventsContainer(new CommunityUpdatedEvent(table.getCommunityModel().getCard(0),
                table.getCommunityModel().getCard(1), table.getCommunityModel().getCard(2))));

        NextBettingRound nextAction = new NextBettingRound();
        nextAction.setTransitionStrategy(() -> context.setState(new Turn()));
        context.setState(nextAction);
    }
}
