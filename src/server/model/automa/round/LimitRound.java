package server.model.automa.round;

import server.controller.Game;
import server.model.PlayerModel;
import server.model.Position;
import server.model.automa.Showdown;

import java.util.ListIterator;

public class LimitRound extends AbstractLimitRound {
    private int nextPosition;

    public LimitRound(int betAndRaiseValue, int nextPosition){
        super(betAndRaiseValue);

        if(nextPosition == Position.SB.ordinal()){
            roundNumber = 0;
        }else{
            roundNumber = 1;
        }
    }

    @Override
    public void goNext(Game game) {

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
                roundNumber++;
            }

        }

        if (table.countPlayersInGame() == 1) {
            game.setNextState(new Showdown());
        } else if ((table.countActivePlayers() == 1 && table.countPlayersAllIn() > 0) || isMatched() || table.isAllPlayersAllIn()) {
            game.setNextState(strategy.determineTransition());
        }
    }
}
