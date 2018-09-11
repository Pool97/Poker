package interfaces;

import java.io.Serializable;

public interface PokerAction extends Serializable {
    void accept(ActionManager actionManager);

    void process(BettingManager bettingManager);

    int getValue();
}
