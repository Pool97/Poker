package server.model;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Model di un generico Player di Poker.
 * Permette di gestire la logica del Player.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class PlayerModel implements Serializable {
    private String nickname;
    private Position position;
    private String avatar;
    private int rank;
    private int chips;
    private ArrayList<Pair<ActionType, Integer>> actions;

    /**
     * Costruttore della classe PlayerModel.
     *
     * @param nickname Nickname del Player
     * @param chips    Numero iniziale di Chips a disposizione
     */

    public PlayerModel(String nickname, int chips) {
        this.nickname = nickname;
        this.chips = chips;
        actions = new ArrayList<>();
    }

    /**
     * Costruttore della classe PlayerModel.
     *
     * @param nickname Nickname del Player
     * @param avatar   Nome del file dell'avatar del Player
     */

    public PlayerModel(String nickname, String avatar) {
        this.nickname = nickname;
        this.avatar = avatar;
        actions = new ArrayList<>();
    }

    /**
     * Costruttore della classe PlayerModel.
     *
     * @param nickname Nickname del Player
     * @param position Posizione iniziale ricoperta dal Player
     */

    public PlayerModel(String nickname, Position position) {
        this.nickname = nickname;
        this.position = position;
        actions = new ArrayList<>();
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Permette, attraverso l'analisi delle mosse effettuate in un determinato turno, di stabilire se
     * il giocatore è in All-In oppure no.
     *
     * @return true se è in All-In, false altrimenti.
     */

    public boolean isAllIn() {
        return actions
                .stream()
                .anyMatch(action -> action.getKey() == ActionType.ALL_IN);
    }

    /**
     * Permette, attraverso l'analisi delle mosse effettuate in un determinato turno, di stabilire se
     * il giocatore ha effettuato il fold nel turno, oppure no.
     *
     * @return true se ha foldato, false altrimenti.
     */

    public boolean hasFolded() {
        return actions.stream()
                .anyMatch(action -> action.getKey() == ActionType.FOLD);
    }

    /**
     * Permette di calcolare e restituire la somma totale delle scommesse effettuate nel turno dal Player.
     *
     * @return Somma totale delle scommesse effettuare nel turno.
     */

    public int getTurnBet() {
        return actions.stream().mapToInt(Pair::getValue).sum();
    }

    /**
     * Permette di aggiungere una nuova mossa alla lista delle mosse effettuate dal player in un determinato turno.
     *
     * @param action Nuova mossa effettuata
     */

    public void addAction(Pair<ActionType, Integer> action) {
        if (chips == action.getValue())
            action = new Pair<>(ActionType.ALL_IN, action.getValue());

        chips -= action.getValue();
        actions.add(action);
    }

    /**
     * Permette di stabilire se il giocatore è ancora in partita oppure se è stato sconfitto.
     * La condizione di sconfitta è data dall'azzeramento del suo chip stack alla fine di un turno.
     * @return True se è stato sconfitto, false se è ancora in gioco.
     */

    public boolean hasLost() {
        return chips == 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerModel that = (PlayerModel) o;
        return rank == that.rank &&
                chips == that.chips &&
                Objects.equals(nickname, that.nickname) &&
                position == that.position &&
                Objects.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, position, avatar, rank, chips);
    }
}
