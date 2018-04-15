package server;

import java.util.ArrayList;

/**
 *
 */

public class PlayerModel {
    private String nickname;
    private TurnPosition turnPosition;
    private StakeAction stakeAction;
    private String avatarFilename;
    private int rank;
    private int totalChips;
    private ArrayList<CardModel> turnCards;
    private ArrayList<StakeAction> turnActions;

    public PlayerModel(String nickname, TurnPosition turnPosition, StakeAction stakeAction, String avatarFilename, int rank, int totalChips) {
        this.nickname = nickname;
        this.turnPosition = turnPosition;
        this.stakeAction = stakeAction;
        this.avatarFilename = avatarFilename;
        this.rank = rank;
        this.totalChips = totalChips;
        turnCards = new ArrayList<>();
        turnActions = new ArrayList<>();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public TurnPosition getTurnPosition() {
        return turnPosition;
    }

    public void setTurnPosition(TurnPosition turnPosition) {
        this.turnPosition = turnPosition;
    }

    public StakeAction getStakeAction() {
        return stakeAction;
    }

    public void setStakeAction(StakeAction stakeAction) {
        this.stakeAction = stakeAction;
    }

    public String getAvatarFilename() {
        return avatarFilename;
    }

    public void setAvatarFilename(String avatarFilename) {
        this.avatarFilename = avatarFilename;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getTotalChips() {
        return totalChips;
    }

    public void setTotalChips(int totalChips) {
        this.totalChips = totalChips;
    }

    public ArrayList<CardModel> getTurnCards() {
        return turnCards;
    }

    public void setTurnCards(ArrayList<CardModel> turnCards) {
        this.turnCards = turnCards;
    }
}
