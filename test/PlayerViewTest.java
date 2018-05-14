import client.view.ActionsView;
import client.view.CommunityField;
import client.view.ParametersView;
import client.view.PlayerView;
import layout.TableLayout;
import layout.TableLayoutConstraints;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;

public class PlayerViewTest {

    public static void main(String[] args) {
        EventQueue.invokeLater(() ->{
            JFrame frame = new JFrame();
            double[][] size = {{TableLayout.FILL}, {0.20, 0.40, 0.20, 0.20}};
            frame.setLayout(new TableLayout(size));
            frame.getContentPane().setBackground(Color.BLUE);
            PlayerView container = new PlayerView("Tizio", 200, "avatars/celebrity/dario zappa.png");
            container.setAvatar("avatars/celebrity/marco campi.png");
            //container.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5, 200));

            JPanel topPlayersPanel = new JPanel();
            topPlayersPanel.setLayout(new BoxLayout(topPlayersPanel, BoxLayout.X_AXIS));
            topPlayersPanel.setBackground(Utils.TRANSPARENT);

            PlayerView player1 = new PlayerView("Tizio", 200, "avatars/celebrity/marco campi.png");
            player1.setAlignmentX(Component.LEFT_ALIGNMENT);
            topPlayersPanel.add(player1);
            topPlayersPanel.add(Box.createRigidArea(new Dimension(300, 10)));
            PlayerView player2 = new PlayerView("Tizio", 200, "avatars/celebrity/marco campi.png");
            player2.setAlignmentX(Component.RIGHT_ALIGNMENT);
            topPlayersPanel.add(player2);

            frame.add(topPlayersPanel, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.CENTER, TableLayoutConstraints.CENTER));


            JPanel middleContainer = new JPanel();
            middleContainer.setLayout(new GridBagLayout());
            middleContainer.setBackground(Utils.TRANSPARENT);

            PlayerView player3 = new PlayerView("Tizio", 200, "avatars/celebrity/marco campi.png");
            PlayerView player4 = new PlayerView("Tizio", 200, "avatars/celebrity/marco campi.png");

            CommunityField community = new CommunityField();

            /*Card card = new Card();
            card.setMaximumSize(new Dimension(200, 200));
            community.addNextCard(card);
            Card card1 = new Card();
            card.setMaximumSize(new Dimension(150, 150));
            community.addNextCard(card1);
            Card card2 = new Card();
            card.setMaximumSize(new Dimension(150, 150));
            community.addNextCard(card2);
            Card card3 = new Card();
            card.setMaximumSize(new Dimension(150, 150));
            community.addNextCard(card3);
            Card card4 = new Card();
            card.setMaximumSize(new Dimension(150, 150));
            community.addNextCard(card4);*/
            middleContainer.add(community, new GBC(1, 0, 0.40, 1, 1, 1, GBC.NORTH, GBC.NONE));

            frame.add(middleContainer, new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.CENTER));


            JPanel topPlayersPanel2 = new JPanel();
            topPlayersPanel2.setLayout(new BoxLayout(topPlayersPanel2, BoxLayout.X_AXIS));
            topPlayersPanel2.setBackground(Utils.TRANSPARENT);

            PlayerView player5 = new PlayerView("Tizio", 200, "avatars/celebrity/marco campi.png");
            player5.setAlignmentX(Component.LEFT_ALIGNMENT);
            topPlayersPanel2.add(player5);
            topPlayersPanel2.add(Box.createRigidArea(new Dimension(300, 0)));
            PlayerView player6 = new PlayerView("Tizio", 200, "avatars/celebrity/marco campi.png");
            player6.setAlignmentX(Component.RIGHT_ALIGNMENT);
            topPlayersPanel2.add(player6);


            frame.add(topPlayersPanel2, new TableLayoutConstraints(0, 2, 0, 2, TableLayoutConstraints.CENTER, TableLayoutConstraints.BOTTOM));


            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setBackground(Utils.TRANSPARENT);


            JButton button = new JButton("ProvaDICOANAGLIADSNFJKJVNVLS");
            panel.add(button, new GBC(0, 0, 25, 1, 1, 1, GBC.NORTHEAST, GBC.BOTH,
                    new Insets(0, 10, 10, 10)));

            ActionsView actionsView = new ActionsView();
            ParametersView parametersView = new ParametersView(0, 0, 0);
            panel.add(actionsView, new GBC(1, 0, 50, 1, 1, 1, GBC.CENTER,
                    GBC.VERTICAL, new Insets(0, 10, 10, 10)));


            panel.add(parametersView, new GBC(3, 0, 10, 1, 1, 1, GBC.NORTHEAST,
                    GBC.VERTICAL, new Insets(0, 10, 10, 10)));


            frame.add(panel, new TableLayoutConstraints(0, 3, 0, 3, TableLayoutConstraints.FULL, TableLayoutConstraints.CENTER));


            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1920, 1080);
            frame.setResizable(false);
            frame.setVisible(true);
            player3.setAlignmentX(Component.LEFT_ALIGNMENT);


            player3.setPreferredSize(player1.getSize());
            player4.setPreferredSize(player1.getSize());
            middleContainer.add(player3, new GBC(2, 0, 0.3, 1, 1, 1, GBC.CENTER, GBC.NONE));
            middleContainer.add(player4, new GBC(0, 0, 0.3, 1, 1, 1, GBC.CENTER, GBC.NONE));

            //player4.setAlignmentX(Component.LEFT_ALIGNMENT);
            //middleContainer.add(player4);


        });
    }
}
