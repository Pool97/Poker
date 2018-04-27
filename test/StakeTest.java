import org.junit.Test;
import server.model.PlayerModel;

import java.util.List;

public class StakeTest {


    @Test
    public void should() {
        /*TurnModel matchModel = new TurnModel();

        matchModel.setStartingChipAmount(10000);
        matchModel.increaseBlinds();

        StakeAction action1 = new StakeAction(ActionType.RAISE, 200);
        //StakeAction action2p = new StakeAction(ActionType.RAISE, 100);
        //StakeAction action2 = new StakeAction(ActionType.CALL, 200);
        StakeAction action2 = new StakeAction(ActionType.FOLD);
        StakeAction action3 = new StakeAction(ActionType.FOLD);
        PlayerModel player1 = new PlayerModel("Pool97", Position.BB, action1, "pool.png", 1, 10000);
        PlayerModel player2 = new PlayerModel("Perry97", Position.D, action2, "perry.png", 2, 9000);
        PlayerModel player3 = new PlayerModel("Tunsi97", Position.SB, action3, "tunsi.png", 3, 8000);
        //player1.addToTurnActions(action2p);
        List<PlayerModel> players = Arrays.asList(player1, player2, player3);
        assertTrue(onePlayerRemained(players));
        assertTrue(isEquityReached(players));*/
    }

    public boolean onePlayerRemained(List<PlayerModel> players) {
        return players.stream().filter(player -> !player.hasFolded()).count() == 1;

    }

    public boolean isEquityReached(List<PlayerModel> players) {
        return players.stream()
                .filter(player -> !player.hasFolded()).noneMatch(player -> player.sumChipsBetted() != 200);
    }
}
