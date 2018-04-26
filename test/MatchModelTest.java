import org.junit.Test;

public class MatchModelTest {
    @Test
    public void shouldMoveAllPlayers(){
        /*MatchModel matchModel = new MatchModel();
        //ArrayList<PlayerModel> testPlayers = matchModel.getPlayers();
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
    public void shouldBigBlindIncrement(){
        /*MatchModel matchModel = new MatchModel();
        new StartTurn(matchModel);
        assertTrue(matchModel.getBigBlind() == 1000);*/
    }

    @Test
    public void shouldSmallBlindIncrement(){
        /*MatchModel matchModel = new MatchModel();
        new StartTurn(matchModel);
        assertTrue(matchModel.getSmallBlind() == 500);*/
    }
}
