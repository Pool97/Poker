import client.view.CommunityField;
import client.view.PlayerView;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ClientCreator {
    public static void main(String[] args) {
        Dimension playersSize = new Dimension(400, 200);

        PlayerView test = new PlayerView(playersSize);
        PlayerView test1 = new PlayerView(playersSize);
        PlayerView test2 = new PlayerView(playersSize);
        PlayerView test3 = new PlayerView(playersSize);
        PlayerView test4 = new PlayerView(playersSize);

        List<PlayerView> playerViewsTest = Arrays.asList(test, test1, test2, test3, test4);

        Dimension cardsSize = new Dimension(130, 180);
        CommunityField testField = new CommunityField(cardsSize);

        EventQueue.invokeLater(() -> {
            //Game game = new Game(playerViewsTest, testField, true, new PlayerCreatedEvent("Pool97", "creator.png"));
            //game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //game.setVisible(true);
        });
    }
}
