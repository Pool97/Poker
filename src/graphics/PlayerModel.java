package graphics;

/**
 * Rappresenta un messaggio proveniente dal Server contenente le informazioni riguardo a un giocatore avversario.
 */

public class PlayerModel {
    private String nickname;
    private String totalChips;
    private String actualPosition;
    private String action;
    private String ranking;
    private String avatarFilename;
    private String firstCardFilename;
    private String secondCardFilename;

    public PlayerModel(String nickname, String totalChips, String actualPosition, String action, String ranking, String firstCardFilename, String secondCardFilename, String avatarFilename) {
        this.nickname = nickname;
        this.totalChips = totalChips;
        this.actualPosition = actualPosition;
        this.action = action;
        this.ranking = ranking;
        this.firstCardFilename = firstCardFilename;
        this.secondCardFilename = secondCardFilename;
        this.avatarFilename = avatarFilename;
    }

    public String getFirstCardFilename() {
        return firstCardFilename;
    }

    public void setFirstCardFilename(String firstCardFilename) {
        this.firstCardFilename = firstCardFilename;
    }

    public String getSecondCardFilename() {
        return secondCardFilename;
    }

    public void setSecondCardFilename(String secondCardFilename) {
        this.secondCardFilename = secondCardFilename;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTotalChips() {
        return totalChips;
    }

    public void setTotalChips(String totalChips) {
        this.totalChips = totalChips;
    }

    public String getActualPosition() {
        return actualPosition;
    }

    public void setActualPosition(String actualPosition) {
        this.actualPosition = actualPosition;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getAvatarFilename() {
        return avatarFilename;
    }

    public void setAvatarFilename(String avatarFilename) {
        this.avatarFilename = avatarFilename;
    }
}
