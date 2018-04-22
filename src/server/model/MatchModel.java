package server.model;

/**
 * Il Model fondamentale del gioco.
 * Permette di tenere traccia di tutti i cambiamenti avvenuti nel Match, dal suo inizio alla sua fine.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class MatchModel {
    private int pot;
    private int smallBlind;
    private int bigBlind;
    private int finalBigBlind;
    private int startingChipAmount;
    private DeckModel deckModel;

    /**
     * Costruttore vuoto della classe MatchModel.
     */

    public MatchModel() {
        pot = 0;
        smallBlind = 0;
        bigBlind = 0;
    }

    public int getPot(){
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

    public void increaseBlinds(){
        if(bigBlind < finalBigBlind) {
            bigBlind *= 2;
            smallBlind = bigBlind / 2;
        }
    }

    /**
     * Permette di resettare a zero il pot.
     * Solitamente questa azione avviene alla fine di ogni turno, quando il pot finale viene riscosso dal Player che si è aggiudicato il turno.
     */

    public void resetPot(){
        pot = 0;
    }

    public int getStartingChipAmount() {
        return startingChipAmount;
    }

    /**
     * Permette di impostare l'importo iniziale di chips-per-player. Attualmente, per semplicità, l'importo è di 20000.
     */


    public void setStartingChipAmount(int startingChipAmount){
        this.startingChipAmount = startingChipAmount;
    }

    /**
     * Permette di impostare i valori iniziali relativi ai bui. Un approccio ragionevole è quello di impostare il Big Blind a un
     * cinquantesimo dell'importo iniziale di chips-per-player, in modo da evitare l'eliminazione quasi immediata di alcuni players dovuta
     * a un valore iniziale troppo elevato del BB.
     * Lo Small Blind viene calcolato di conseguenza.
     */

    public void setInitialBlinds(){
        bigBlind = startingChipAmount / 50;
        smallBlind = bigBlind / 2;
    }

    /**
     * Permette di impostare il limite massimo ai valori che può assumere il Big Blind.
     * Il limite coincide con l'importo iniziale di chips-per-player.
     */

    public void setFinalBigBlind(){
        finalBigBlind = startingChipAmount;
    }
}
