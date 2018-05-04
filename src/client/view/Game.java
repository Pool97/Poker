package client.view;

import client.socket.ClientManager;
import client.socket.SocketReader;
import events.*;
import interfaces.EventProcess;
import interfaces.Message;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;


public class Game extends JFrame{
    private ClientManager clientManager;
    private SocketReader<? extends Message> reader;
    private JPanel boardPanel;
    private JPanel topPlayersPanel;
    private JPanel middleContainer;
    private List<PlayerView> playerViews;
    private CommunityField communityField;

    public final static String FIELD_IMAGE = "board.png";
    public final static String CARD_BACK = "backBluePP.png";

    public Game(ClientManager manager) {
        setLookAndFeel();
        this.clientManager = manager;
        this.reader = new SocketReader<>(clientManager.getInputStream());
        reader.setEventProcess(new EventProcessor());

        this.playerViews = Arrays.asList(new PlayerView(new Dimension(400, 200)), new PlayerView(new Dimension(400, 200)),
                new PlayerView(new Dimension(400, 200)), new PlayerView(new Dimension(400, 200)),
                new PlayerView(new Dimension(400, 200)), new PlayerView(new Dimension(400, 200)));
        this.communityField = new CommunityField(new Dimension(130, 180));

        middleContainer= new JPanel();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setVisible(true);

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

    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    class EventProcessor implements EventProcess {

        public EventProcessor() {

        }

        @Override
        public void process(ActionOptionsEvent event) {
            System.out.println("ActionOptionsEvent!");
        }

        @Override
        public void process(BlindsUpdatedEvent event) {
            System.out.println("BlindsUpdatedEvent!");
        }

        @Override
        public void process(PlayerAddedEvent event) {
            PlayerView playerView = playerViews.stream()
                    .filter(view -> view.getNickname().getText().isEmpty()).findFirst().get();
            playerView.setAvatar(event.getAvatar());
            playerView.setNickname(event.getNickname().toUpperCase());
            playerView.setTotalChips(event.getInitialChips());
        }

        @Override
        public void process(PlayerUpdatedEvent event) {
            System.out.println("PlayerUpdatedEvent!");
        }

        @Override
        public void process(PotUpdatedEvent event) {
            System.out.println("PotUpdatedEvent!");
        }

        @Override
        public void process(RoomCreatedEvent event) {

        }
    }
}
