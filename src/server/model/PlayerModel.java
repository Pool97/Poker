package server.model;

import javafx.util.Pair;

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
    private Position position;
    private String avatarFilename;
    private int rank;
    private int totalChips;
    private ArrayList<Pair<ActionType, Integer>> actions;

    public PlayerModel(String nickname, int totalChips) {
        this.nickname = nickname;
        this.totalChips = totalChips;
        actions = new ArrayList<>();
    }

    public PlayerModel(String nickname, String avatarFilename){
        this.nickname = nickname;
        this.avatarFilename = avatarFilename;
        actions = new ArrayList<>();
    }

    public PlayerModel(String nickname, Position position) {
        this.nickname = nickname;
        this.position = position;
        actions = new ArrayList<>();
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarFilename() {
        return avatarFilename;
    }

    public void setTotalChips(int totalChips) {
        this.totalChips = totalChips;
    }

    public int getTotalChips() {
        return totalChips;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isAllIn() {
        return actions
                .stream()
                .anyMatch(action -> action.getKey() == ActionType.ALL_IN);
    }

    public boolean hasFolded() {
        return actions.stream()
                .anyMatch(action -> action.getKey() == ActionType.FOLD);
    }

    public int getTurnBet() {
        return actions.stream().mapToInt(Pair::getValue).sum();
    }

    public void addAction(Pair<ActionType, Integer> action) {
        if (totalChips == action.getValue())
            action = new Pair<>(ActionType.ALL_IN, action.getValue());

        totalChips -= action.getValue();
        actions.add(action);
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
