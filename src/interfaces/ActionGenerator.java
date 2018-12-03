package interfaces;

public interface ActionGenerator {

    PokerAction retrieveRaise();
    PokerAction retrieveCall();
    PokerAction retrieveBet();
    PokerAction retrieveFold();
    PokerAction retrieveCheck();
    PokerAction retrieveAllIn();

}
