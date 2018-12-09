package server.model.automa;

import interfaces.Reacher;
import server.model.gamestructure.FixedLimit;
import server.model.gamestructure.NoLimit;

public class ReacherAdapter implements Reacher {

    @Override
    public void nextState(NoLimit type) {

    }

    @Override
    public void nextState(FixedLimit type) {

    }
}
