import org.junit.Test;

public class MatchModelTest {
    @Test
    public void shouldMoveAllPlayers(){
        /*MatchModel matchModel = new MatchModel();
        //ArrayList<PlayerModel> testPlayers = matchModel.getPlayers();
        PlayerModel player1 = new PlayerModel("Player1", PlayerPosition.D);
        PlayerModel player2 = new PlayerModel("Player2", PlayerPosition.SB);
        PlayerModel player3 = new PlayerModel("Player3", PlayerPosition.BB);
        PlayerModel player4 = new PlayerModel("Player4", PlayerPosition.UTG);
        PlayerModel player5 = new PlayerModel("Player5", PlayerPosition.HJ);
        PlayerModel player6 = new PlayerModel("Player6", PlayerPosition.CO);
        testPlayers.add(player1);
        testPlayers.add(player2);
        testPlayers.add(player3);
        testPlayers.add(player4);
        testPlayers.add(player5);
        testPlayers.add(player6);
        //matchModel.movePlayersPosition();

        assertTrue(player1.getTurnPosition() == PlayerPosition.SB);
        assertTrue(player2.getTurnPosition() == PlayerPosition.BB);
        assertTrue(player3.getTurnPosition() == PlayerPosition.UTG);
        assertTrue(player4.getTurnPosition() == PlayerPosition.HJ);
        assertTrue(player5.getTurnPosition() == PlayerPosition.CO);
        assertTrue(player6.getTurnPosition() == PlayerPosition.D);*/

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
