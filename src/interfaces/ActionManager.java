package interfaces;

import server.model.actions.*;

public interface ActionManager {
    void process(Call call);

    void process(RaiseNoLimit raiseNoLimitOption);

    void process(Fold fold);

    void process(Check check);

    void process(BetNoLimit betNoLimit);

    void process(AllIn allin);

    void process(RaiseLimit raiseLimit);

    void process(BetLimit betLimit);

    void process(AbstractPokerAction action);
}
