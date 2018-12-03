package interfaces;

import server.model.actions.*;

public interface ActionGenerator {

    RaiseOption retrieveRaise();
    Call retrieveCall();
    BetOption retrieveBetOption();
    Fold retrieveFold();
    Check retrieveCheck();
}
