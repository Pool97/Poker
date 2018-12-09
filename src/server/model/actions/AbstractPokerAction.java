package server.model.actions;

import interfaces.ActionManager;
import interfaces.PokerAction;

public class AbstractPokerAction implements PokerAction {
    private String nickname;
    private int value;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void accept(ActionManager actionManager) {
        actionManager.process(this);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
