package server.model.automa;

import interfaces.PokerState;
import server.model.Dealer;
import server.model.Table;

public abstract class AbstractPokerState implements PokerState {
    protected static Table table;
    protected static Dealer dealer;

    public AbstractPokerState(){

    }

    public AbstractPokerState(Table table, Dealer dealer){
        this.table = table;
        this.dealer = dealer;
    }
}
