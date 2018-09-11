package server.model;

import interfaces.PokerAction;
import server.model.actions.AllIn;
import server.model.actions.Bet;
import server.model.actions.DeadMoney;
import server.model.actions.Fold;
import server.model.cards.CardModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class PlayerModel implements Serializable, Cloneable {
    private String nickname;
    private Position position;
    private String avatar;
    private int chips;
    private boolean hasLost;
    private boolean isDisconnected;
    private boolean isCreator;
    private ArrayList<CardModel> cards;
    private ArrayList<PokerAction> actions;

    public PlayerModel(String nickname, String avatar) {
        this.nickname = nickname;
        this.avatar = avatar;
        actions = new ArrayList<>();
        cards = new ArrayList<>();
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

    public void addChips(int chips) {
        this.chips += chips;
    }
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void addCard(CardModel card) {
        cards.add(card);
    }

    public ArrayList<CardModel> getCards() {
        return cards;
    }

    public void removeCards() {
        cards.clear();
    }

    public void removeActions() {
        actions.clear();
    }

    public boolean isAllIn() {
        return actions
                .stream()
                .anyMatch(action -> action instanceof AllIn);
    }

    public boolean hasFolded() {
        return actions.stream()
                .anyMatch(action -> action instanceof Fold);
    }

    public Stream<CardModel> cardsStream() {
        return cards.stream();
    }

    public int getTurnBet() {
        return actions.stream().mapToInt(PokerAction::getValue).sum();
    }

    public int getTurnBetWithoutDeadMoney() {
        return actions.stream().filter(action -> !(action instanceof DeadMoney)).mapToInt(PokerAction::getValue).sum();
    }

    public void addAction(PokerAction action) {
        if (chips == action.getValue())
            action = new AllIn(action.getValue());
        if (!(action instanceof DeadMoney)) {
            chips -= action.getValue();
        }
        actions.add(action);
    }

    public boolean hasLost() {
        return hasLost;
    }

    public boolean hasBetted() {
        return actions.stream().anyMatch(action -> (action instanceof Bet));
    }

    public void setLost(boolean hasLost) {
        this.hasLost = hasLost;
    }

    public boolean isDisconnected() {
        return isDisconnected;
    }

    public void setDisconnected(boolean isDisconnected) {
        this.isDisconnected = isDisconnected;
    }

    public boolean isCreator() {
        return isCreator;
    }

    public void setCreator(boolean isCreator) {
        this.isCreator = isCreator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerModel that = (PlayerModel) o;
        return chips == that.chips &&
                Objects.equals(nickname, that.nickname) &&
                position == that.position &&
                Objects.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, position, avatar, chips);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
