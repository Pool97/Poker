import javafx.util.Pair;
import org.junit.Test;
import server.model.ActionType;
import server.model.PlayerModel;
import server.model.TurnModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class StakeTest {
    private TurnModel turnModel;

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

        turnModel = new TurnModel();
        PlayerModel player1 = new PlayerModel("Pool97", 10000);
        PlayerModel player2 = new PlayerModel("Perry97", 9000);
        PlayerModel player3 = new PlayerModel("Tunsi97", 8000);
        PlayerModel player4 = new PlayerModel("RedMax", 5000);

        turnModel.addAction(player1, new Pair<>(ActionType.CALL, 100));
        /*turnModel.addAction(player1, new StakeAction(ActionType.CALL, 100));
        turnModel.addAction(player1, new StakeAction(ActionType.FOLD, 100));
        turnModel.addAction(player2, new StakeAction(ActionType.FOLD, 200));
        turnModel.addAction(player3, new StakeAction(ActionType.CALL, 200));
        turnModel.addAction(player4, new StakeAction(ActionType.ALL_IN, 100));*/
        assertTrue(turnModel.getTurnBet(player1) == 100);


    }


    public boolean isEquityReached(List<PlayerModel> players) {
        ArrayList<PlayerModel> playerInGame = players.stream()
                .filter(player -> !turnModel.hasPlayerFolded(player)).collect(Collectors.toCollection(ArrayList::new));

        ArrayList<PlayerModel> allInPlayers = playerInGame.stream()
                .filter(player -> turnModel.hasPlayerAllIn(player)).collect(Collectors.toCollection(ArrayList::new));

        int betMaxAllIn = allInPlayers.stream().mapToInt(player -> turnModel.getTurnBet(player)).max().orElse(0);

        ArrayList<PlayerModel> playerInGameNotInAllIn = playerInGame.stream()
                .filter(player -> !turnModel.hasPlayerAllIn(player)).collect(Collectors.toCollection(ArrayList::new));

        System.out.println(playerInGameNotInAllIn.size());
        if (playerInGameNotInAllIn.stream().filter(player -> turnModel.getTurnBet(player) < betMaxAllIn).count() > 0)
            return false;

        long distinct = playerInGameNotInAllIn
                .stream().mapToInt(player -> turnModel.getTurnBet(player)).distinct().count();
        System.out.println(distinct);
        return distinct <= 1;
    }
}
