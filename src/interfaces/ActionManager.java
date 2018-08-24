package interfaces;

import server.model.*;

public interface ActionManager {
    void process(Call call);

    void process(RaiseOption raiseOption);

    void process(Fold fold);

    void process(Check check);

    void process(BetOption bet);
}
