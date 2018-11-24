package server.model.automa;

import server.events.EventsContainer;
import server.events.PlayerUpdatedEvent;
import server.events.PotUpdatedEvent;
import server.model.PlayerModel;
import server.model.actions.Call;

/**
 * Questo è lo stato dell'automa che si occupa di riscuotere le puntate obbligatorie del turno, ossia lo Small Blind e
 * il Big Blind.
 * Informa successivamente a tutti i Players delle modifiche apportate alle chips dei PlayerBoard in SB e BB e delle
 * modifiche apportate al valore del pot.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public abstract class Blind extends AbstractPokerState {

    public Blind() {
    }


    protected void payBlindAndUpdate(PlayerModel player, int value) {
        player.addAction(new Call(value));
        PlayerUpdatedEvent event = new PlayerUpdatedEvent(player.getNickname(), player.getChips(), "PAY " + value);
        table.sendBroadcast(new EventsContainer(event));
    }

    protected void increasePotAndUpdate(int quantity) {
        int potIncreased = table.increasePot(quantity);
        table.sendBroadcast(new EventsContainer(new PotUpdatedEvent(potIncreased)));
    }
}
