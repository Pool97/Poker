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
import java.util.ArrayList;


public class Game extends JFrame{
    private JPanel boardPanel;
    private JPanel topPlayersPanel;
    private JPanel middleContainer;
    private ArrayList<PlayerView> playerViews;
    private CommunityField communityField;

    public final static String FIELD_IMAGE = "board.png";
    public final static String CARD_BACK = "backBluePP.png";

    public Game(ArrayList<PlayerView> playerViews, CommunityField communityField){
        this.playerViews = playerViews;
        this.communityField = communityField;
        middleContainer= new JPanel();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);

        initBoardPanel();
        initTopPlayersPanel();
        initMiddleContainer();

        boardPanel.add(new JButton(""), new GBC(0, 0, 1, 20));

        GBC secondConstr = new GBC(0, 1, 2, 1, 1, 25);
        secondConstr.anchor = GBC.CENTER;
        secondConstr.fill = GBC.BOTH;
        boardPanel.add(topPlayersPanel, secondConstr);

        GBC thirdConstr = new GBC(0, 2, 1, 25);
        thirdConstr.anchor = GBC.NORTH;
        thirdConstr.fill = GBC.BOTH;
        boardPanel.add(middleContainer, thirdConstr);

        boardPanel.add(new JButton("Interfaccia utente"), new GBC(0,3, 1, 30));
    }

    private void initBoardPanel(){
        GridBagLayout mainLayout = new GridBagLayout();
        BufferedImage board = Utils.loadImage(FIELD_IMAGE, getSize());
        boardPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(board, 0, 0, getSize().width, getSize().height, null);
            }
        };

        boardPanel.setBackground(Color.DARK_GRAY);
        boardPanel.setLayout(mainLayout);
        add(boardPanel);
    }

    private void initTopPlayersPanel(){
        topPlayersPanel = new JPanel();
        topPlayersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, (getSize().width - 3 * playerViews.get(0).getPreferredSize().width)/3, 0));
        topPlayersPanel.add(playerViews.get(0));
        topPlayersPanel.setBackground(Utils.TRANSPARENT);
        for(int i = 0; i < 3; i++){
            playerViews.get(i).setAlignmentX(Component.CENTER_ALIGNMENT);
            topPlayersPanel.add(playerViews.get(i));
        }

    }

    private void initMiddleContainer(){
        middleContainer.setLayout(new BoxLayout(middleContainer, BoxLayout.X_AXIS));
        middleContainer.setBackground(Utils.TRANSPARENT);
        middleContainer.add(Box.createHorizontalGlue());
        System.out.println(playerViews.get(3).getNickname().getText());
        System.out.println(playerViews.get(4).getNickname().getText());
        playerViews.get(3).setAlignmentX(Component.LEFT_ALIGNMENT);
        middleContainer.add(playerViews.get(3));
        middleContainer.add(Box.createHorizontalGlue());

        communityField.setAlignmentX(Component.CENTER_ALIGNMENT);
        middleContainer.add(communityField);
        middleContainer.add(Box.createHorizontalGlue());

        playerViews.get(4).setAlignmentX(Component.RIGHT_ALIGNMENT);
        middleContainer.add(playerViews.get(4));
        middleContainer.add(Box.createHorizontalGlue());

    }


    public static void main(String [] args){

        //Creazione di Players di test
        Dimension playersSize = new Dimension(400, 200);
        PlayerView test = new PlayerView(playersSize, new PlayerModelTest("HARRY POTTER", "1000000$",
                "D", "FOLD", "1","backOrangePP.png", "backOrangePP.png", "avatar.png"));
        PlayerView test1 = new PlayerView(playersSize, new PlayerModelTest("HERMIONE", "60000$", "SB", "CALL", "2",
                "backOrangePP.png", "backOrangePP.png", "avatar2.png"));

        PlayerView test2 = new PlayerView(playersSize, new PlayerModelTest("TIZIO", "40000$", "BB", "CHECK",
                "3", "backOrangePP.png", "backOrangePP.png", "zappa_avatar.png"));

        PlayerView test3 = new PlayerView(playersSize, new PlayerModelTest("CAIO", "60000$", "SB", "CALL",
                "2","backOrangePP.png", "backOrangePP.png", "avatar3.png"));
        PlayerView test4 = new PlayerView(playersSize, new PlayerModelTest("SEMPRONIO", "60000$", "SB", "CALL",
                "2","backOrangePP.png", "backOrangePP.png", "avatar4.png"));

        ArrayList<PlayerView> playerViewsTest = new ArrayList<>();
        playerViewsTest.add(test);
        playerViewsTest.add(test1);
        playerViewsTest.add(test2);
        playerViewsTest.add(test3);
        playerViewsTest.add(test4);

        Dimension cardsSize = new Dimension(130, 180);
        CommunityField testField = new CommunityField(cardsSize);
        testField.addNextCard(new CardView(cardsSize, "2CuoriR.png", "backBluePP.png"));
        testField.addNextCard(new CardView(cardsSize, "7PiccheN.png", "backBluePP.png"));
        testField.addNextCard(new CardView(cardsSize, "AQuadriR.png", "backBluePP.png"));
        testField.addNextCard(new CardView(cardsSize, "KCuoriR.png", "backBluePP.png"));
        testField.addNextCard(new CardView(cardsSize, "8FioriN.png", "backBluePP.png"));

        EventQueue.invokeLater(() -> {
            Game game = new Game(playerViewsTest, testField);
            game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            game.setVisible(true);
        });

    }
}
