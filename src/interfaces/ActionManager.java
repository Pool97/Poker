package interfaces;

import server.model.Call;
import server.model.Check;
import server.model.Fold;
import server.model.RaiseOption;

public interface ActionManager {
    void process(Call call);

    void process(RaiseOption raiseOption);

    void process(Fold fold);

    void process(Check check);
}
