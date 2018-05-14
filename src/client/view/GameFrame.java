package client.view;

import client.frames.EventProcessAdapter;
import client.socket.ClientManager;
import client.socket.SocketReader;
import client.socket.SocketWriter;
import events.*;
import interfaces.Message;
import javafx.util.Pair;
import layout.TableLayout;
import layout.TableLayoutConstraints;
import server.model.ActionType;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GameFrame extends JFrame {
    public final static String FIELD_IMAGE = "board.png";
    public final static String CARD_BACK = "backBluePP.png";
    private ClientManager clientManager;
    private SocketReader<? extends Message> reader;
    private JPanel boardPanel;
    private List<PlayerView> players;
    private ParametersView parametersView;
    private CommunityField communityField;
    private JPanel topTable;
    private JPanel middleTable;
    private JPanel bottomTable;
    private ActionsView actionsView;

    public GameFrame(ClientManager manager) {
        this.clientManager = manager;
        this.reader = new SocketReader<>(clientManager.getInputStream());
        reader.setEventProcess(new EventProcessor());
        reader.execute();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        players = new ArrayList<>();

        initBoardPanel();
        initTopPlayersPanel();
        initMiddleContainer();
        initTopPlayersPanel2();
        initSlider();

        setSize(screenSize);
        setResizable(false);
        setVisible(true);

    }

    private void initBoardPanel() {
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Utils.loadImage(FIELD_IMAGE, getSize()), 0, 0, getWidth(), getHeight(), null);
            }
        };

        double[][] size = {{TableLayout.FILL}, {0.20, 0.40, 0.20, 0.20}};
        boardPanel.setLayout(new TableLayout(size));
        boardPanel.setBackground(Color.DARK_GRAY);
        add(boardPanel);
    }

    private void initTopPlayersPanel() {
        topTable = new JPanel();
        topTable.setLayout(new BoxLayout(topTable, BoxLayout.X_AXIS));
        topTable.setBackground(Utils.TRANSPARENT);
        boardPanel.add(topTable, new TableLayoutConstraints(0, 0, 0, 0,
                TableLayout.CENTER, TableLayout.CENTER));
    }

    private void initTopPlayersPanel2() {
        bottomTable = new JPanel();
        bottomTable.setLayout(new BoxLayout(bottomTable, BoxLayout.X_AXIS));
        bottomTable.setBackground(Utils.TRANSPARENT);
        boardPanel.add(bottomTable, new TableLayoutConstraints(0, 2, 0, 2,
                TableLayout.CENTER, TableLayout.BOTTOM));
    }

    private void initMiddleContainer() {
        middleTable = new JPanel();
        double[][] size = {{0.3, 0.4, 0.3}, {TableLayout.FILL}};
        middleTable.setLayout(new TableLayout(size));
        middleTable.setBackground(Utils.TRANSPARENT);

        CommunityField community = new CommunityField();
        middleTable.add(community, new TableLayoutConstraints(1, 0, 1, 0, TableLayout.CENTER, TableLayout.TOP));

        boardPanel.add(middleTable, new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.CENTER));
    }

    private void initSlider() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(Utils.TRANSPARENT);

        JButton button = new JButton("Placeholder");
        bottomPanel.add(button, new GBC(0, 0, 25, 1, 1, 1, GBC.NORTHEAST, GBC.BOTH,
                new Insets(0, 10, 10, 10)));

        actionsView = new ActionsView();
        parametersView = new ParametersView(0, 0, 0);

        bottomPanel.add(actionsView, new GBC(1, 0, 50, 1, 1, 1, GBC.CENTER,
                GBC.VERTICAL, new Insets(0, 10, 10, 10)));

        bottomPanel.add(parametersView, new GBC(3, 0, 10, 1, 1, 1, GBC.NORTHEAST,
                GBC.VERTICAL, new Insets(0, 10, 10, 10)));

        boardPanel.add(bottomPanel, new TableLayoutConstraints(0, 3, 0, 3, TableLayoutConstraints.FULL, TableLayoutConstraints.CENTER));
    }

    private void addPlayerToBoard(PlayerView playerLogged) {
        switch (players.size()) {
            case 0:
                playerLogged.setAlignmentX(Component.LEFT_ALIGNMENT);
                topTable.add(playerLogged);
                break;
            case 1:
                topTable.add(Box.createRigidArea(new Dimension(300, 10)));
                playerLogged.setAlignmentX(Component.RIGHT_ALIGNMENT);
                topTable.add(playerLogged);
                break;
            case 2:
                playerLogged.setPreferredSize(players.get(0).getSize());
                middleTable.add(playerLogged, new TableLayoutConstraints(2, 0, 2, 0, TableLayout.CENTER, TableLayout.CENTER));
                break;
            case 3:
                bottomTable.setAlignmentX(Component.LEFT_ALIGNMENT);

                bottomTable.add(playerLogged);
                break;
            case 4:
                bottomTable.add(Box.createRigidArea(new Dimension(300, 10)));
                playerLogged.setAlignmentX(Component.RIGHT_ALIGNMENT);
                bottomTable.add(playerLogged);
                break;
            case 5:
                playerLogged.setPreferredSize(players.get(0).getSize());
                middleTable.add(playerLogged, new TableLayoutConstraints(0, 0, 0, 0, TableLayout.CENTER, TableLayout.CENTER));
                break;
        }
    }

    class EventProcessor extends EventProcessAdapter {
        @Override
        public void process(ActionOptionsEvent event) {
            int callValue = 0;
            for (int i = 0; i < event.getOptions().size(); i++)
                System.out.println("Azioni disponibile: " + event.getOptions().get(i).getKey() + " " + event.getOptions().get(i).getValue());

            for (int i = 0; i < event.getOptions().size(); i++) {
                final Pair<ActionType, Integer> action = event.getOptions().get(i);
                if (action.getKey() == ActionType.CALL) {
                    callValue = action.getValue();
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
                    actionsView.setMinSliderValue(callValue + 1);
                    actionsView.setMaxSliderValue(action.getValue());
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
            PlayerView playerLogged = new PlayerView(event.getNickname(), event.getInitialChips(),
                    event.getAvatar());
            addPlayerToBoard(playerLogged);
            players.add(playerLogged);
        }

        @Override
        public void process(PlayerUpdatedEvent event) {
            System.out.println("Entrato" + event.getPlayer().getNickname());
            PlayerView player = players.stream()
                    .filter(playerView -> playerView.getNickname().equalsIgnoreCase(event.getPlayer().getNickname())).findFirst().get();
            //player.setPosition(event.getPlayer().getPosition().name());
            System.out.println(event.getPlayer().getChips());
            player.setChips(event.getPlayer().getChips());
        }

        @Override
        public void process(PotUpdatedEvent event) {
            parametersView.setPot(event.getPot());
        }
    }
}
