package client.ui.frames;

import client.events.ConcreteActionManager;
import client.events.ConfirmNewMatchEvent;
import client.events.CreatorRestartEvent;
import client.events.EventsAdapter;
import client.net.ClientManager;
import client.net.SocketReader;
import client.net.SocketWriter;
import client.ui.components.GameBoard;
import client.ui.components.MatchBoard;
import client.ui.components.PlayerBoard;
import client.ui.table.PokerTable;
import client.ui.userboard.ActionBoard;
import interfaces.ActionManager;
import server.events.*;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class BoardFrame extends JFrame {
    private GameBoard gameBoard;
    private PokerTable pokerTable;
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
        gameBoard.attach(actionBoard, matchBoard);
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
        public void process(ServerClosedEvent event) {
            clientManager.close();
            WelcomeFrame.launchGame();
            dispose();
        }

        @Override
        public void process(RestartMatchEvent event) {
            int result = JOptionPane.showConfirmDialog(null, "Vuoi rifare una nuova partita?", "New Match", JOptionPane.YES_NO_OPTION);
            SocketWriter socketWriter;
            if (result == JOptionPane.YES_OPTION) {
                socketWriter = new SocketWriter(clientManager.getOutputStream(), new EventsContainer(new CreatorRestartEvent(true)));
            } else {
                socketWriter = new SocketWriter(clientManager.getOutputStream(), new EventsContainer(new CreatorRestartEvent(false)));
            }
            socketWriter.execute();
        }

        @Override
        public void process(PlayerTurnEvent event) {
            logAvailableActions(event);
            //PlayerBoard playerBoard = pokerTable.getPlayerBoardBy(event.getPlayerNickname());
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
            System.out.println("STAMPA BASTARDONE");
            PlayerBoard playerBoardLogged;
            playerBoardLogged = new PlayerBoard(event.getNickname(), event.getPosition().name(), true, event.getChips(), event.getAvatar());
            if (event.getNickname().equalsIgnoreCase(nickname)) {
                playerBoardLogged.setNicknameColor(Color.YELLOW);
            }
            pokerTable.sit(playerBoardLogged);

        }

        @Override
        public void process(PlayerHasWinEvent event) {
            pokerTable.removePlayer(pokerTable.getPlayerBoardBy(event.getNickname()));
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
            if (pokerTable.isPlayerPresent(event.getNickname()))
                pokerTable.removePlayer(pokerTable.getPlayerBoardBy(event.getNickname()));
        }

        @Override
        public void process(ShowdownEvent event) {
            pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.coverCards(false));
        }

        @Override
        public void process(TurnEndedEvent event) {
            pokerTable.getCommunityCardsBoard().hideAllCards();
            pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.coverCards(true));
            pokerTable.getPlayerBoard().forEach(playerBoard -> playerBoard.setHandIndicator(Utils.EMPTY));
        }

        @Override
        public void process(NewMatchEvent event) {
            int result = JOptionPane.showConfirmDialog(null, "Vuoi rifare una nuova partita se il creator accetta?", "New Match", JOptionPane.YES_NO_OPTION);
            SocketWriter newMatch;
            if (result == JOptionPane.NO_OPTION) {
                clientManager.close();
                WelcomeFrame.launchGame();
                dispose();
            } else {
                newMatch = new SocketWriter(clientManager.getOutputStream(), new EventsContainer(new ConfirmNewMatchEvent(true)));
                newMatch.execute();
                JOptionPane.showMessageDialog(null, "Resta in attesa allora shish!");
            }
        }
    }
}
