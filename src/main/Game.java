package main;

import graphics.CommunityField;
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
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        System.out.println(getSize().width);
        players.setLayout(new FlowLayout(FlowLayout.CENTER, (getSize().width - 3 * 400) / 4, 0));
        players.add(new PlayerView(new Dimension(400, 150), "HARRY POTTER", "1000000$", "SB", "FOLD", "1"));
        players.add(new PlayerView(new Dimension(400, 150), "HARRY POTTER", "1000000$", "SB", "FOLD", "1"));
        players.add(new PlayerView(new Dimension(400, 150), "HARRY POTTER", "1000000$", "SB", "FOLD", "1"));
        container2 = new JPanel();
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
        add(new JButton("Qua ci andranno le informazione della partitaaaaaaa"), firstConstraint);
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
