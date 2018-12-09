package interfaces;

public interface TransitionStrategy<PokerState> {
    PokerState determineTransition();
}
