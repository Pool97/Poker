package server.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Model di un generico Player di Poker.
 * Permette di gestire la logica del Player, come ad esempio determinare se è ancora in partita
 * oppure se ha perso.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PlayerModel implements Serializable {
    private String nickname;
    private PlayerPosition turnPosition;
    private StakeAction stakeAction;
    private String avatarFilename;
    private int rank;
    private int totalChips;
    private ArrayList<StakeAction> turnActions;

    public PlayerModel(String nickname, PlayerPosition turnPosition, StakeAction stakeAction, String avatarFilename, int rank, int totalChips) {
        this.nickname = nickname;
        this.turnPosition = turnPosition;
        this.stakeAction = stakeAction;
        this.avatarFilename = avatarFilename;
        this.rank = rank;
        this.totalChips = totalChips;
        turnActions = new ArrayList<>();
    }

    public PlayerModel(String nickname, String avatarFilename){
        this.nickname = nickname;
        this.avatarFilename = avatarFilename;
    }

    public PlayerModel(String nickname, PlayerPosition turnPosition) {
        this.nickname = nickname;
        this.turnPosition = turnPosition;
    }

    public void setTotalChips(int totalChips) {
        this.totalChips = totalChips;
    }

    public PlayerPosition getTurnPosition() {
        return turnPosition;
    }

    public void setTurnPosition(PlayerPosition turnPosition) {
        this.turnPosition = turnPosition;
    }

    /**
     * Permette di stabilire se il giocatore è ancora in partita oppure se è stato sconfitto.
     * La condizione di sconfitta è data dall'azzeramento del suo chip stack alla fine di un turno.
     * @return True se è stato sconfitto, false se è ancora in gioco.
     */

    public boolean hasLost(){
        return totalChips == 0;
    }
}
