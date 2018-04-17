package server.model;

import java.util.ArrayList;

/**
 * Il Model fondamentale del gioco.
 * Permette di tenere traccia di tutti i cambiamenti avvenuti nel Match, dal suo inizio alla
 * sua fine.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class MatchModel {
    private int pot;
    private int smallBlind;
    private int bigBlind;
    private ArrayList<PlayerModel> players;

    public static final int CONST = 1000;

    public MatchModel() {
        players = new ArrayList<>();
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

    public void increaseBigBlind(){
        bigBlind += CONST; //TODO: per adesso incremento di 1000 il BB per ogni turno, SB = BB/2
        //TODO: trovare l'algoritmo appropriato per la modifica di big e small blind
    }

    public void increaseSmallBlind(){
        smallBlind = bigBlind / 2;
    }

    public void resetPot(){
        pot = 0;
    }
}
