package client.frames;

import client.ConcreteActionManager;
import client.components.*;
import client.events.EventsAdapter;
import client.socket.ClientManager;
import client.socket.SocketReader;
import interfaces.ActionManager;
import server.events.*;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;


public class BoardFrame extends JFrame {
    private GameBoard gameBoard;
    private PokerTable pokerTable;
    private JButton placeHolder;
    private ActionBoard actionBoard;
    private MatchBoard matchBoard;
    private ClientManager clientManager;
    private String nickname;

    public BoardFrame(ClientManager manager, String nickname) {
        this.clientManager = manager;
        this.nickname = nickname;
        System.out.println("Sono io: " + nickname);
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
        actionBoard = new ActionBoard();
        matchBoard = new MatchBoard();
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
        gameBoard.attach(placeHolder, actionBoard, matchBoard);
    }

    private void attachBoard() {
        add(gameBoard);
    }

    private void setFrameProperties() {
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientManager.close();
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void listenToServer() {
        SocketReader reader = new SocketReader(clientManager.getInputStream());
        reader.setEventsManager(new ConcreteEventManager());
        reader.execute();
    }

    private void logAvailableActions(PlayerTurnEvent event) {
        ClientManager.logger.info("Azioni disponibili: " + event.getPlayerNickname() + " \n");
        event.getOptions()
                .forEach(action -> ClientManager.logger.info("Azione: " + action.toString()));
    }

    class ConcreteEventManager extends EventsAdapter {
        private ActionManager actionManager;

        public ConcreteEventManager() {
            actionManager = new ConcreteActionManager(clientManager, actionBoard);
        }

        @Override
        public void process(PlayerTurnEvent event) {
            logAvailableActions(event);
            PlayerBoard playerBoard = pokerTable.getPlayerBoardBy(event.getPlayerNickname());
            //playerBoard.activateColorTransition();

            if (event.getPlayerNickname().equalsIgnoreCase(nickname))
                event.getOptions().forEach(action -> action.accept(actionManager));
        }

        @Override
        public void process(BlindsUpdatedEvent event) {
            matchBoard.setSmallBlind(event.getSmallBlind());
            matchBoard.setBigBlind(event.getBigBlind());
        }

        @Override
        public void process(PlayerLoggedEvent event) {
            PlayerBoard playerBoardLogged;
            playerBoardLogged = new PlayerBoard(event.getNickname(), event.getPosition().name(), true, event.getChips(), event.getAvatar());
            if (event.getNickname().equalsIgnoreCase(nickname)) {
                playerBoardLogged.setNicknameColor(Color.YELLOW);
            }
            pokerTable.sit(playerBoardLogged);
        }

        @Override
        public void process(PlayerHasWinEvent event) {
            if (nickname.equals(event.getNickname())) {
                JOptionPane.showMessageDialog(null, "Hai vinto mentekatto!!xDxD");
            }
        }

        @Override
        public void process(PlayerHasLostEvent event) {
            if (nickname.equals(event.getNickname())) {
                JOptionPane.showMessageDialog(null, "Hai perso mentekatto!!xDxD");
            }

            pokerTable.removePlayer(pokerTable.getPlayerBoardBy(event.getNickname()));
        }

        @Override
        public void process(PlayerUpdatedEvent event) {
            pokerTable.updatePlayerProperties(event);

        }

        @Override
        public void process(PlayerFoldedEvent event) {
            pokerTable.getPlayerBoardBy(event.getNickname()).setHandIndicator("FOLD");
        }

        @Override
        public void process(PotUpdatedEvent event) {
            matchBoard.setPot(event.getPot());
        }

        @Override
        public void process(CommunityUpdatedEvent event) {
            while (event.number() != 0)
                pokerTable.addCardToCommunityCardsBoard(event.getCard().getImageDirectoryPath());
        }

        @Override
        public void process(TurnStartedEvent event) {
            PlayerBoard playerBoard = pokerTable.getPlayerBoardBy(event.getNickname());
            playerBoard.setPosition(event.getTurnPosition());
            playerBoard.assignNewCards(event.getFrontImageCards().get(0), event.getFrontImageCards().get(1));

            if (event.getNickname().equalsIgnoreCase(nickname))
                playerBoard.coverCards(false);

        }

        @Override
        public void process(PlayerDisconnectedEvent event) {
            pokerTable.removePlayer(pokerTable.getPlayerBoardBy(event.getNickname()));
        }

        @Override
        public void process(ShowdownEvent event) {
            pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.coverCards(false));
            for (Map.Entry<String, String> entry : event.getPoints().entrySet())
                pokerTable.getPlayerBoard().stream().filter(playerBoard -> playerBoard.getNickname().equals(entry.getKey()))
                        .forEach(playerBoard -> playerBoard.setHandIndicator(entry.getValue()));
        }

        @Override
        public void process(TurnEndedEvent event) {
            pokerTable.getCommunityCardsBoard().hideAllCards();
            pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.coverCards(true));
            pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.setHandIndicator(Utils.EMPTY));
        }
    }
}
