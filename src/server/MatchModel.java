package server;

/**
 * Il Model fondamentale del gioco.
 * È colui che gestisce l'inizio e la fine del match, come anche il proseguire di tutto il Match,
 * turno per turno.
 */

public class MatchModel {
    private int pot;
    private int smallBlind;
    private int bigBlind;

    public MatchModel(){

    }


    public void setPot(int pot) {
        this.pot = pot;
    }

    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }
}
