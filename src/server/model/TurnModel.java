package server.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Il Model fondamentale del gioco.
 * Permette di tenere traccia di tutti i cambiamenti avvenuti nel Match, dal suo inizio alla sua fine.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class TurnModel {
    private int pot;
    private HashMap<PlayerModel, ArrayList<Pair<ActionType, Integer>>> turnActions;
    private DeckModel deckModel;

    /**
     * Costruttore vuoto della classe TurnModel.
     */

    public TurnModel() {
        pot = 0;
        turnActions = new HashMap<>();

    }

    public int getPot() {
        return pot;
    }

    /**
     * Permette di resettare a zero il pot.
     * Solitamente questa azione avviene alla fine di ogni turno, quando il pot finale viene riscosso dal Player che si è aggiudicato il turno.
     */

    public void resetPot() {
        pot = 0;
    }

    /**
     * Permette di incrementare il pot di una quantità fornita in ingresso al metodo.
     *
     * @param quantity Valore da aggiungere al pot
     */
    public int increasePot(int quantity) {
        pot += quantity;
        return pot;
    }


    public void addAction(PlayerModel player, Pair<ActionType, Integer> action) {
        turnActions.putIfAbsent(player, new ArrayList<>());

        if (player.getTotalChips() == action.getValue())
            action = new Pair<>(ActionType.ALL_IN, action.getValue());

        player.setTotalChips(player.getTotalChips() - action.getValue());
        turnActions.get(player).add(action);
    }

    public boolean hasPlayerFolded(PlayerModel player) {
        return turnActions.get(player).stream()
                .anyMatch(action -> action.getKey() == ActionType.FOLD);
    }

    public boolean hasPlayerAllIn(PlayerModel player) {
        return turnActions.get(player)
                .stream()
                .anyMatch(action -> action.getKey() == ActionType.ALL_IN);
    }

    public int getTurnBet(PlayerModel player) {
        return turnActions.get(player).stream().mapToInt(Pair::getValue).sum();
    }
}
