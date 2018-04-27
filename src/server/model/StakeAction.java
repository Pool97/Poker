package server.model;

import java.io.Serializable;

public class StakeAction implements Serializable {
    private ActionType type;
    private int stakeChips;

    public StakeAction(ActionType type, int stakeChips) {
        this.type = type;
        this.stakeChips = stakeChips;
    }

    public StakeAction(ActionType type) {
        this.type = type;
        stakeChips = 0;
    }

    public ActionType getType() {
        return type;
    }

    public int getStakeChips() {
        return stakeChips;
    }

    public void setStakeChips(int stakeChips) {
        this.stakeChips = stakeChips;
    }
}
