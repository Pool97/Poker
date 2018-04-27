package server.model;

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
    private int smallBlind;
    private int bigBlind;
    private int startingChipAmount;
    private DeckModel deckModel;

    /**
     * Costruttore vuoto della classe TurnModel.
     */

    public TurnModel() {
        pot = 0;
        smallBlind = 0;
        bigBlind = 0;
    }

    public int getPot() {
        return pot;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    /**
     * Permette di incrementare i bui in ogni turno. Per non rendere troppo lunga la partita ogni Big Blind
     * sarà il doppio di quello precedente. Lo Small Blind si calcola di conseguenza, essendo da regolamento
     * ufficiale la metà del Big Blind. Ovviamente è ragionevole porre un limite all'aumento del Big Blind, esso
     * non può assumere valori che sono al di sopra dell'importo iniziale di chips-per-player.
     */

    public void increaseBlinds() {
        if (bigBlind == 0) {
            bigBlind = startingChipAmount / 50;
            smallBlind = bigBlind / 2;
        }
        if (bigBlind < startingChipAmount) {
            bigBlind *= 2;
            smallBlind = bigBlind / 2;
        }
    }

    /**
     * Permette di resettare a zero il pot.
     * Solitamente questa azione avviene alla fine di ogni turno, quando il pot finale viene riscosso dal Player che si è aggiudicato il turno.
     */

    public void resetPot() {
        pot = 0;
    }

    public int getStartingChipAmount() {
        return startingChipAmount;
    }

    /**
     * Permette di impostare l'importo iniziale di chips-per-player.
     *
     * @param startingChipAmount Importo iniziale di chips-per-player
     */

    public void setStartingChipAmount(int startingChipAmount) {
        this.startingChipAmount = startingChipAmount;
    }

    /**
     * Permette di incrementare il pot di una quantità fornita in ingresso al metodo.
     *
     * @param quantity Valore da aggiungere al pot
     */
    public void increasePot(int quantity) {
        pot += quantity;
    }

}
