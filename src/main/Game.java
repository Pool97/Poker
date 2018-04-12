package main;

import graphics.CommunityField;
import graphics.PlayerModel;
import graphics.PlayerView;
import utils.GBC;

import javax.swing.*;
import java.awt.*;


public class Game extends JFrame{
    private JLabel info;
    private JPanel container;
    private JPanel container2;

    public Game(){
        GridBagLayout mainLayout = new GridBagLayout();
        setLayout(mainLayout);
        JPanel players = new JPanel();
        getContentPane().setBackground(new Color(27, 94, 32));
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        players.setLayout(new FlowLayout(FlowLayout.CENTER, (getSize().width - 3 * 400) / 4, 0));
        PlayerView test = new PlayerView(new Dimension(400, 200), new PlayerModel("HARRY POTTER", "1000000$", "SB", "FOLD", "1", "2_cuori.png", "2_quadri1.png"));
        players.add(test);
        players.add(new PlayerView(new Dimension(400, 200), new PlayerModel("HARRY POTTER", "1000000$", "SB", "FOLD", "1", "2_cuori.png", "2_quadri1.png")));
        players.add(new PlayerView(new Dimension(400, 200), new PlayerModel("HARRY POTTER", "1000000$", "SB", "FOLD", "1", "2_cuori.png", "2_quadri1.png")));
        players.setBackground(new Color(27, 94, 32));
        container2 = new JPanel();
        container2.setBackground(new Color(27, 94, 32));
        container2.add(new CommunityField());
        container2.setLayout(new FlowLayout());

        GBC firstConstraint = new GBC(0, 0, 50, 30);
        GBC secondConstraint = new GBC(0, 2, 100, 100);
        secondConstraint.fill = GBC.BOTH;
        secondConstraint.anchor = GBC.NORTH;
        GBC thirdConstraint = new GBC(0, 1, 100, 30);
        thirdConstraint.anchor = GBC.CENTER;
        thirdConstraint.fill = GBC.BOTH;

        add(container2, secondConstraint);
        add(new JButton(""), firstConstraint);
        add(players, thirdConstraint);
        JLabel community = new JLabel("Community Cards", JLabel.CENTER);
        community.setFont(new Font("Helvetica", Font.PLAIN, 40));
    }

    public static void main(String [] args){
        EventQueue.invokeLater(() -> {
            Game game = new Game();
            game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            game.setVisible(true);
        });

    }
}
