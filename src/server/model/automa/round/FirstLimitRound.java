package server.model.automa.round;

import interfaces.PokerState;
import interfaces.TransitionStrategy;
import server.controller.Game;
import server.model.PlayerModel;
import server.model.Position;
import server.model.automa.Showdown;

import java.util.ListIterator;

public class FirstLimitRound extends LimitRound {
    private TransitionStrategy<PokerState> strategy;

    public FirstLimitRound(int betAndRaiseValue){
        super(betAndRaiseValue);
    }

    @Override
    public void goNext(Game game) {
        int nextPosition = (Position.BB.ordinal() + 1) % table.size();

        ListIterator<PlayerModel> iterator = nextPosition == 0 ? table.iterator() : table.iterator(Position.BB.ordinal());
        PlayerModel player;

        while (!roundFinished(nextPosition)) {

            player = iterator.next();

            if (!player.hasFolded() && !player.isAllIn()) {
                doAction(player, game);
            }

            nextPosition = iterator.nextIndex();

            if(!iterator.hasNext()){
                iterator = table.iterator();
                nextPosition = Position.SB.ordinal();
            }

        }

        if (table.countPlayersInGame() == 1) {
            game.setState(new Showdown());
        } else if ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) || isMatched() || table.isAllPlayersAllIn()) {
            game.setState(strategy.determineTransition());
        }
    }

    public void setTransitionStrategy(TransitionStrategy<PokerState> strategy){
        this.strategy = strategy;
    }

    protected boolean roundFinished(int cursor){
        return (cursor == Position.SB.ordinal() && isMatched())
                || (cursor != Position.SB.ordinal()) && (table.countPlayersInGame() == 1 ||
                ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) && !checkForActingPlayer())
                || table.isAllPlayersAllIn());
    }

}
