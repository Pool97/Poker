package main;

import graphics.CommunityField;
import graphics.Field;
import graphics.GBC;

import javax.swing.*;
import java.awt.*;


public class Game extends JFrame{
    private JLabel info;
    private JPanel container;
    private JPanel container2;

    public Game(){
        info = new JLabel("Benvenuto sciocco al mio nuovo giuoco");
        container2 = new JPanel();
        container2.add(new Field(container2.getSize()));
        container2.setLayout(new FlowLayout());
        GridBagLayout mainLayout = new GridBagLayout();
        setLayout(mainLayout);
        GBC firstConstraint = new GBC(0, 1, 1, 1, 100, 100);
        GBC secondConstraint = new GBC(0, 2, 1, 1, 100, 100);
        GBC thirdConstraint = new GBC(0, 0, 1, 1, 100, 100);
        firstConstraint.setFill(GBC.HORIZONTAL);
        setSize(1366, 768);
        setLayout(new GridBagLayout());

        add(container2, secondConstraint);
        add(new CommunityField(), firstConstraint);
        add(info, thirdConstraint);
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
