package interfaces;

import server.model.actions.*;

public interface BettingManager {
    void process(Fold fold);

    void process(Check check);

    void process(RaiseNoLimit raiseNoLimit);

    void process(Call call);

    void process(BetNoLimit betNoLimit);
}
