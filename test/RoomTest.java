import org.junit.Test;
import server.model.PlayerModel;
import server.model.Position;
import server.model.PositionManager;

import java.util.ArrayList;
import java.util.Comparator;

import static junit.framework.TestCase.assertTrue;

public class RoomTest {

    @Test
    public void playersShouldBeSorted() {
        ArrayList<PlayerModel> players = new ArrayList<>();
        PlayerModel player1 = new PlayerModel("Pool97", Position.BB);
        PlayerModel player2 = new PlayerModel("Perry97", Position.D);
        PlayerModel player3 = new PlayerModel("Tunsi97", Position.SB);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.sort(Comparator.comparing(PlayerModel::getPosition));
        assertTrue(players.get(0).getPosition() == Position.D);
        assertTrue(players.get(1).getPosition() == Position.SB);
        assertTrue(players.get(2).getPosition() == Position.BB);

        PositionManager manager = new PositionManager();
        manager.addPositions(6);
        System.out.println(manager.toString());
        System.out.println(manager.nextPosition(Position.D));
        System.out.println(manager.nextPosition(Position.SB));
        System.out.println(manager.nextPosition(Position.CO));
        manager.removePosition();
        System.out.println(manager.nextPosition(Position.BB));
        /*manager.shift();
        System.out.println(manager.toString());
        manager.shift();
        System.out.println(manager.toString());*/
    }
}
