import org.junit.Test;
import server.model.PlayerModel;

import static org.junit.Assert.assertEquals;

public class PlayerModelTest {

    @Test
    public void shouldAllIn() {
        PlayerModel player = new PlayerModel("Pool97", 10000);
        //player.addToTurnActions(new StakeAction(ActionType.CALL, 11000));
        //assertTrue(player.isAllIn() == true);
        assertEquals(player.getTotalChips(), 0);
    }
}
