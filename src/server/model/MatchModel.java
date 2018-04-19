package server.model;

import java.util.ArrayList;

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
    private ArrayList<PlayerModel> players;
    private DeckModel deckModel;
    public final static int STARTING_CHIP_AMOUNT = 20000;

    /**
     * Costruttore vuoto della classe MatchModel.
     */

    public MatchModel() {
        players = new ArrayList<>();
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

    public ArrayList<PlayerModel> getPlayers() {
        return players;
    }

    /**
     * Permette di traslare di una posizione tutti i Players del Match.
     * Esempio: se alla fine del turno un Player era il D (= Dealer) nel prossimo turno diventerà SB
     * ( = Small Blind) e così via per tutti i Players.
     */

    public void movePlayersPosition(){
        for(int i = 0; i < players.size(); i++){
            PlayerModel playerModel = players.get(i);
            PlayerPosition actualPosition = playerModel.getTurnPosition();
            if(i == PlayerPosition.CO.ordinal())
                playerModel.setTurnPosition(PlayerPosition.D);
            else
                playerModel.setTurnPosition(PlayerPosition.values()[actualPosition.ordinal() + 1]);
        }
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

    /**
     * Permette di impostare l'importo iniziale di chips-per-player. Attualmente, per semplicità, l'importo è di 20000.
     */

    //TODO: permettere agli utenti di scegliere l'importo iniziale di chips-per-player.

    public void setStartingChipAmount(){
        players.stream().forEach(playerModel -> playerModel.setTotalChips(STARTING_CHIP_AMOUNT));
    }

    /**
     * Permette di impostare i valori iniziali relativi ai bui. Un approccio ragionevole è quello di impostare il Big Blind a un
     * cinquantesimo dell'importo iniziale di chips-per-player, in modo da evitare l'eliminazione quasi immediata di alcuni players dovuta
     * a un valore iniziale troppo elevato del BB.
     * Lo Small Blind viene calcolato di conseguenza.
     */

    public void setInitialBlinds(){
        bigBlind = STARTING_CHIP_AMOUNT / 50;
        smallBlind = bigBlind / 2;
    }

    /**
     * Permette di impostare il limite massimo ai valori che può assumere il Big Blind.
     * Il limite coincide con l'importo iniziale di chips-per-player.
     */

    public void setFinalBigBlind(){
        finalBigBlind = STARTING_CHIP_AMOUNT;
    }
}
