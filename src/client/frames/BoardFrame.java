package client.frames;

import client.components.*;
import client.socket.ClientManager;
import client.socket.SocketReader;
import client.socket.SocketWriter;
import events.*;
import interfaces.Message;
import javafx.util.Pair;
import server.model.ActionType;

import javax.swing.*;
import java.awt.*;


public class BoardFrame extends JFrame {
    private GameBoard gameBoard;
    private PokerTable pokerTable;
    private JButton placeHolder;
    private UserActionBoard userActionBoard;
    private MatchBoard matchBoard;
    private ClientManager clientManager;

    public BoardFrame(ClientManager manager) {
        this.clientManager = manager;

        createBoard();
        createPokerTable();
        createUserBoard();

        attachComponents();
        setFrameProperties();
        listenToServer();
    }

    private void createBoard() {
        gameBoard = new GameBoard();
    }

    private void createPokerTable() {
        pokerTable = new PokerTable();
    }

    private void createUserBoard() {
        placeHolder = new JButton("Placeholder");
        userActionBoard = new UserActionBoard();
        matchBoard = new MatchBoard(0, 0, 0);
    }

    private void attachComponents() {
        attachPokerTable();
        attachUserBoard();
        attachBoard();
    }

    private void attachPokerTable() {
        gameBoard.attach(pokerTable);
    }

    private void attachUserBoard() {
        gameBoard.attach(placeHolder, userActionBoard, matchBoard);
    }

    private void attachBoard() {
        add(gameBoard);
    }

    private void setFrameProperties() {
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);
        setVisible(true);
    }

    private void listenToServer() {
        SocketReader<? extends Message> reader = new SocketReader<>(clientManager.getInputStream());
        reader.setEventProcess(new EventProcessor());
        reader.execute();
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
                    userActionBoard.setCallActionListener(eventG -> {
                        SocketWriter<? extends Message> called = new SocketWriter<>(clientManager.getOutputStream(),
                                new Events(new ActionPerformedEvent(new Pair<>(ActionType.CALL, action.getValue()))));
                        called.execute();
                    });
                } else if (action.getKey() == ActionType.CHECK) {
                    userActionBoard.setCheckActionListener(eventG -> {
                        SocketWriter<? extends Message> called = new SocketWriter<>(clientManager.getOutputStream(),
                                new Events(new ActionPerformedEvent(new Pair<>(ActionType.CHECK, 0))));
                        called.execute();
                    });
                } else if (action.getKey() == ActionType.RAISE) {
                    userActionBoard.setMinSliderValue(callValue + 1);
                    userActionBoard.setMaxSliderValue(action.getValue());
                    userActionBoard.setRaiseActionListener(eventG -> {
                        SocketWriter<? extends Message> called = new SocketWriter<>(clientManager.getOutputStream(),
                                new Events(new ActionPerformedEvent(new Pair<>(ActionType.RAISE, userActionBoard.getSliderValue()))));
                        called.execute();
                    });
                }
            }
        }

        @Override
        public void process(BlindsUpdatedEvent event) {
            matchBoard.setSmallBlind(event.getSmallBlind());
            matchBoard.setBigBlind(event.getBigBlind());
        }

        @Override
        public void process(PlayerAddedEvent event) {
            PlayerBoard playerBoardLogged = new PlayerBoard(event.getNickname(), event.getInitialChips(), event.getAvatar());
            pokerTable.sit(playerBoardLogged);

        }

        @Override
        public void process(PlayerUpdatedEvent event) {
            PlayerBoard playerBoard = pokerTable.getPlayerBoards().stream()
                    .filter(playerView -> playerView.getNickname().equalsIgnoreCase(event.getPlayer().getNickname())).findFirst().get();
            //playerBoard.setPosition(event.getPlayer().getPosition().name());
            System.out.println(event.getPlayer().getChips());
            playerBoard.setChips(event.getPlayer().getChips());
        }

        @Override
        public void process(PotUpdatedEvent event) {
            matchBoard.setPot(event.getPot());
        }
    }
}
