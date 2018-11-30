package server.model;

import server.model.cards.CardModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerModel implements Serializable, Cloneable{
    private String nickname;
    private Position position;
    private String avatar;
    private int chips;
    private boolean isAllIn;
    private boolean hasFolded;
    private boolean hasLost;
    private boolean isDisconnected;
    private boolean isCreator;
    private Hand hand;

    public PlayerModel(String nickname, String avatar) {
        this.nickname = nickname;
        this.avatar = avatar;
        hand = new Hand();

    }

    public PlayerModel(){
        hand = new Hand();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public void receiveCards(CardModel first, CardModel second) {
        hand.addCards(first, second);
    }

    public ArrayList<CardModel> getCards() {
        return hand.getHand();
    }

    public void giveBackCards() {
        hand.giveBackCards();
    }

    public void setAllIn(boolean isAllIn){
        this.isAllIn = isAllIn;
    }

    public boolean isAllIn() {
        return isAllIn;
    }

    public void setFolded(boolean hasFolded){
        this.hasFolded = hasFolded;
    }

    public boolean hasFolded() {
        return hasFolded;
    }

    public boolean hasLost() {
        return hasLost;
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

    public void decreaseChips(int value){
        chips = chips - value;
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
