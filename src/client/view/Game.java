package client.view;

import client.frames.EventProcessAdapter;
import client.socket.ClientManager;
import client.socket.SocketReader;
import client.socket.SocketWriter;
import events.*;
import interfaces.Message;
import javafx.util.Pair;
import server.model.ActionType;
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
    private JPanel topPlayersPanel2;
    private JPanel middleContainer;
    private List<PlayerView> playerViews;
    private ParametersView parametersView;
    private CommunityField communityField;
    private ActionsView actionsView;

    public final static String FIELD_IMAGE = "board.png";
    public final static String CARD_BACK = "backBluePP.png";

    public Game(ClientManager manager) {
        this.clientManager = manager;
        this.reader = new SocketReader<>(clientManager.getInputStream());
        reader.setEventProcess(new EventProcessor());
        reader.execute();

        this.playerViews = Arrays.asList(new PlayerView(new Dimension(400, 200)),
                new PlayerView(new Dimension(400, 200)),
                new PlayerView(new Dimension(400, 200)),
                new PlayerView(new Dimension(400, 200)),
                new PlayerView(new Dimension(400, 200)),
                new PlayerView(new Dimension(400, 200)));

        this.communityField = new CommunityField(new Dimension(130, 180));

        middleContainer= new JPanel();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setVisible(true);
        initBoardPanel();
        initTopPlayersPanel();
        initMiddleContainer();
        initTopPlayersPanel2();

        GBC gbc = new GBC(0, 0, 1, 10);
        gbc.anchor = GBC.NORTH;
        this.parametersView = new ParametersView(0, 0, 0);
        initSlider();

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

        playerViews.stream().limit(2).forEach(playerView -> {
            playerView.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPlayersPanel.add(playerView);
        });

        GBC constraint = new GBC(0, 0, 2, 1, 1, 15);
        constraint.anchor = GBC.CENTER;
        boardPanel.add(topPlayersPanel, constraint);
    }

    private void initTopPlayersPanel2() {
        topPlayersPanel2 = new JPanel();
        topPlayersPanel2.setLayout(new FlowLayout(FlowLayout.CENTER, (getSize().width - 3 * playerViews.get(4).getPreferredSize().width) / 3, 0));
        topPlayersPanel2.add(playerViews.get(4));
        topPlayersPanel2.add(playerViews.get(5));
        topPlayersPanel2.setBackground(Utils.TRANSPARENT);


        GBC constraint = new GBC(0, 2, 2, 1, 1, 15);
        constraint.anchor = GBC.CENTER;
        boardPanel.add(topPlayersPanel2, constraint);
    }

    private void initMiddleContainer(){
        middleContainer.setLayout(new GridBagLayout());
        middleContainer.setBackground(Utils.TRANSPARENT);

        GBC verticalConstr = new GBC(0, 0);
        verticalConstr.weightx = 0.25;
        verticalConstr.weighty = 1;
        verticalConstr.fill = GBC.BOTH;
        JPanel verticalContainer = new JPanel();
        verticalContainer.setLayout(new BoxLayout(verticalContainer, BoxLayout.Y_AXIS));
        verticalContainer.setBackground(Utils.TRANSPARENT);
        PlayerView playerView2 = playerViews.get(2);
        playerView2.setAlignmentX(Component.CENTER_ALIGNMENT);
        verticalContainer.add(Box.createVerticalGlue());
        verticalContainer.add(playerView2);
        verticalContainer.add(Box.createVerticalGlue());
        middleContainer.add(verticalContainer, verticalConstr);

        GBC communityConstr = new GBC(1, 0);
        communityConstr.weightx = 0.75;
        communityConstr.fill = GBC.BOTH;
        communityConstr.insets = new Insets(20, 0, 20, 0);
        middleContainer.add(communityField, communityConstr);


        GBC verticalConstr2 = new GBC(2, 0);
        verticalConstr2.weightx = 0.25;
        verticalConstr2.weighty = 1;
        verticalConstr2.fill = GBC.BOTH;
        JPanel verticalContainer2 = new JPanel();
        verticalContainer2.setLayout(new BoxLayout(verticalContainer2, BoxLayout.Y_AXIS));
        verticalContainer2.setBackground(Utils.TRANSPARENT);
        PlayerView playerView3 = playerViews.get(3);
        playerView3.setAlignmentX(Component.CENTER_ALIGNMENT);
        verticalContainer2.add(Box.createVerticalGlue());
        verticalContainer2.add(playerView3);
        verticalContainer2.add(Box.createVerticalGlue());
        middleContainer.add(verticalContainer2, verticalConstr2);

        GBC constraint = new GBC(0, 1, 1, 60);
        constraint.anchor = GBC.NORTH;
        constraint.fill = GBC.BOTH;
        boardPanel.add(middleContainer, constraint);
    }

    private void initSlider() {

        GBC gbcs = new GBC(0, 3, 1, 10);
        gbcs.fill = GBC.BOTH;

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Utils.TRANSPARENT);


        JButton button = new JButton("Prova");
        panel.add(button, new GBC(0, 0, 25, 1, 1, 1, GBC.CENTER, GBC.BOTH,
                new Insets(0, 10, 0, 10)));

        actionsView = new ActionsView();

        panel.add(actionsView, new GBC(1, 0, 50, 1, 1, 1, GBC.CENTER,
                GBC.BOTH, new Insets(0, 10, 0, 10)));

        panel.add(new JButton("Prova"), new GBC(2, 0, 15, 1, 1, 1, GBC.CENTER, GBC.HORIZONTAL,
                new Insets(0, 10, 0, 10)));

        panel.add(parametersView, new GBC(3, 0, 10, 1, 1, 1, GBC.CENTER,
                GBC.BOTH, new Insets(0, 10, 0, 10)));

        boardPanel.add(panel, gbcs);
    }

    class EventProcessor extends EventProcessAdapter {
        @Override
        public void process(ActionOptionsEvent event) {
            for (int i = 0; i < event.getOptions().size(); i++)
                System.out.println("Azioni disponibile: " + event.getOptions().get(i).getKey() + " " + event.getOptions().get(i).getValue());

            for (int i = 0; i < event.getOptions().size(); i++) {
                final Pair<ActionType, Integer> action = event.getOptions().get(i);
                if (action.getKey() == ActionType.CALL) {
                    actionsView.setCallActionListener(eventG -> {
                        SocketWriter<? extends Message> called = new SocketWriter<>(clientManager.getOutputStream(),
                                new Events(new ActionPerformedEvent(new Pair<>(ActionType.CALL, action.getValue()))));
                        called.execute();
                    });
                } else if (action.getKey() == ActionType.CHECK) {
                    actionsView.setCheckActionListener(eventG -> {
                        SocketWriter<? extends Message> called = new SocketWriter<>(clientManager.getOutputStream(),
                                new Events(new ActionPerformedEvent(new Pair<>(ActionType.CHECK, 0))));
                        called.execute();
                    });
                } else if (action.getKey() == ActionType.RAISE) {
                    actionsView.setRaiseActionListener(eventG -> {
                        SocketWriter<? extends Message> called = new SocketWriter<>(clientManager.getOutputStream(),
                                new Events(new ActionPerformedEvent(new Pair<>(ActionType.RAISE, actionsView.getSliderValue()))));
                        called.execute();
                    });
                }
            }
        }

        @Override
        public void process(BlindsUpdatedEvent event) {
            parametersView.setSmallBlind(event.getSmallBlind());
            parametersView.setBigBlind(event.getBigBlind());
        }

        @Override
        public void process(PlayerAddedEvent event) {
            PlayerView player = playerViews
                    .stream()
                    .filter(playerView -> playerView.getNickname().getText().equals(""))
                    .findFirst()
                    .get();

            player.setNickname(event.getNickname());
            player.setAvatar(event.getAvatar());
            player.setChips(event.getInitialChips());
            player.setPosition(event.getPosition().name());
        }

        @Override
        public void process(PlayerUpdatedEvent event) {
            System.out.println("Entrato" + event.getPlayer().getNickname());
            PlayerView player = playerViews.stream()
                    .filter(playerView -> playerView.getNickname().getText().equals(event.getPlayer().getNickname())).findFirst().get();
            player.setPosition(event.getPlayer().getPosition().name());
            System.out.println(event.getPlayer().getChips());
            player.setChips(event.getPlayer().getChips());
        }

        @Override
        public void process(PotUpdatedEvent event) {
            parametersView.setPot(event.getPot());
        }
    }
}
