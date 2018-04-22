package server.model;

public class StakeAction {
    private ActionType type;
    private int stakeChips;

    public StakeAction(ActionType type, int stakeChips) {
        this.type = type;
        this.stakeChips = stakeChips;
    }

    public StakeAction(ActionType type) {
        this.type = type;
        if(type == ActionType.CHECK || type == ActionType.FOLD)
            stakeChips = 0;
    }

    public void setStakeChips(int stakeChips) {
        this.stakeChips = stakeChips;
    }
}
