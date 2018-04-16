package server;

/**
 *
 */

public class StateManager implements Observer {
    private MatchModel matchModel;

    public StateManager(){
        matchModel = new MatchModel();
        StartMatch startMatch = new StartMatch(matchModel);
        startMatch.attach(this);
    }

    @Override
    public void update(Observable observable) {
        if(observable instanceof StartMatch) {
            StartTurn startTurn = new StartTurn(matchModel);
            startTurn.attach(this);
        }
        if(observable instanceof StartTurn)
            new StakeTurn();

        if(observable instanceof StakeTurn)
            new ShowdownTurn();
    }
}
