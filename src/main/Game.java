package main;

import graphics.CardView;
import graphics.CommunityField;
import graphics.PlayerView;
import utils.GBC;
import utils.PlayerModelTest;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Game extends JFrame{
    private JLabel info;
    private JPanel container;
    private JPanel container2;

    public Game(){
        GridBagLayout mainLayout = new GridBagLayout();
        BufferedImage board = Utils.loadImage("board2.png", new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height));
        Graphics g = board.getGraphics();
        JPanel containerOfAll = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(board, 0, 0, getContentPane().getSize().width, getContentPane().getSize().height, null);
            }
        };

        containerOfAll.setBackground(Color.DARK_GRAY);
        containerOfAll.setLayout(mainLayout);
        add(containerOfAll);
        JPanel players = new JPanel();
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        players.setLayout(new FlowLayout(FlowLayout.CENTER, (getSize().width - 3 * 400) / 4, 0));
        PlayerView test = new PlayerView(new Dimension(400, 200), new PlayerModelTest("HARRY POTTER", "1000000$", "D", "FOLD", "1", "backOrangePP.png", "backOrangePP.png", "avatar.png"));
        players.add(test);
        players.add(new PlayerView(new Dimension(400, 200), new PlayerModelTest("HERMIONE", "60000$", "SB", "CALL", "2", "backOrangePP.png", "backOrangePP.png", "avatar2.png")));
        players.add(new PlayerView(new Dimension(400, 200), new PlayerModelTest("TIZIO", "40000$", "BB", "CHECK", "3", "backOrangePP.png", "backOrangePP.png", "zappa_avatar.png")));
        players.setBackground(new Color(0,0,0,0));
        JPanel container3 = new JPanel();
        BoxLayout box = new BoxLayout(container3, BoxLayout.X_AXIS);
        container3.setLayout(box);
        container3.setBackground(new Color(0,0,0,0));
        PlayerView playerFour = new PlayerView(new Dimension(400, 200), new PlayerModelTest("CAIO", "60000$", "SB", "CALL", "2","backOrangePP.png", "backOrangePP.png", "avatar3.png"));
        playerFour.setAlignmentX(Component.LEFT_ALIGNMENT);
        container3.add(Box.createHorizontalGlue());
        container3.add(playerFour);
        container3.add(Box.createHorizontalGlue());
        CommunityField field  = new CommunityField();
        field.addNextCard(new CardView(new Dimension(130, 180), "2CuoriR.png", "backBluePP.png"));
        field.addNextCard(new CardView(new Dimension(130, 180), "7PiccheN.png", "backBluePP.png"));
        field.addNextCard(new CardView(new Dimension(130, 180), "AQuadriR.png", "backBluePP.png"));
        field.addNextCard(new CardView(new Dimension(130, 180), "KCuoriR.png", "backBluePP.png"));
        field.addNextCard(new CardView(new Dimension(130, 180), "8FioriN.png", "backBluePP.png"));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        container3.add(field);
        container3.add(Box.createHorizontalGlue());
        GBC firstConstraint = new GBC(0, 0, 50, 30);
        GBC thirdConstraint = new GBC(0, 1, 2, 1, 100, 30);
        thirdConstraint.anchor = GBC.CENTER;
        thirdConstraint.fill = GBC.BOTH;
        PlayerView playerFive  = new PlayerView(new Dimension(400, 200), new PlayerModelTest("SEMPRONIO", "60000$", "SB", "CALL", "2","backOrangePP.png", "backOrangePP.png", "avatar4.png"));
        playerFive.setAlignmentX(Component.RIGHT_ALIGNMENT);
        container3.add(playerFive);
        container3.add(Box.createHorizontalGlue());
        GBC fourthConstraint = new GBC(0, 2, 100, 100 );
        fourthConstraint.anchor = GBC.NORTHWEST;
        fourthConstraint.fill = GBC.HORIZONTAL;
        containerOfAll.add(container3, fourthConstraint);
        containerOfAll.add(new JButton(""), firstConstraint);
        containerOfAll.add(players, thirdConstraint);
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
