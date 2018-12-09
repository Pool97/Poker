package interfaces;

import server.model.gamestructure.FixedLimit;
import server.model.gamestructure.NoLimit;

public interface Reacher {
    void nextState(NoLimit type);
    void nextState(FixedLimit type);
}
