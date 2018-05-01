import org.junit.Test;

public class TurnModelTest {
    @Test
    public void shouldMoveAllPlayers() {
        /*TurnModel matchModel = new TurnModel();
        //ArrayList<PlayerModel> testPlayers = matchModel.getOrderedPlayers();
        PlayerModel player1 = new PlayerModel("Player1", Position.D);
        PlayerModel player2 = new PlayerModel("Player2", Position.SB);
        PlayerModel player3 = new PlayerModel("Player3", Position.BB);
        PlayerModel player4 = new PlayerModel("Player4", Position.UTG);
        PlayerModel player5 = new PlayerModel("Player5", Position.HJ);
        PlayerModel player6 = new PlayerModel("Player6", Position.CO);
        testPlayers.add(player1);
        testPlayers.add(player2);
        testPlayers.add(player3);
        testPlayers.add(player4);
        testPlayers.add(player5);
        testPlayers.add(player6);
        //matchModel.movePlayersPosition();

        assertTrue(player1.getPosition() == Position.SB);
        assertTrue(player2.getPosition() == Position.BB);
        assertTrue(player3.getPosition() == Position.UTG);
        assertTrue(player4.getPosition() == Position.HJ);
        assertTrue(player5.getPosition() == Position.CO);
        assertTrue(player6.getPosition() == Position.D);*/

    }

    @Test
    public void shouldBigBlindIncrement() {
        /*TurnModel matchModel = new TurnModel();
        new TurnStart(matchModel);
        assertTrue(matchModel.getBigBlind() == 1000);*/
    }

    @Test
    public void shouldSmallBlindIncrement() {
        /*TurnModel matchModel = new TurnModel();
        new TurnStart(matchModel);
        assertTrue(matchModel.getSmallBlind() == 500);*/
    }
}
