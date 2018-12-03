package client.ui.frames;

import client.events.ConcreteActionManager;
import client.events.EventsAdapter;
import client.net.Client;
import client.net.ReadServerMessagesTask;
import client.ui.components.GameBoard;
import client.ui.components.MatchBoard;
import client.ui.components.PlayerBoard;
import client.ui.dialogs.LoserDialog;
import client.ui.dialogs.WinnerDialog;
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

    public BoardFrame() {

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
                Client.getInstance().close();
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void listenToServer() {
        ReadServerMessagesTask reader = new ReadServerMessagesTask();
        reader.setEventsManager(new ConcreteEventManager());
        reader.execute();
    }

    private void logAvailableActions(PlayerTurnEvent event) {
        Client.logger.info("Azioni disponibili: " + event.getPlayerNickname() + " \n");
        event.getOptions()
                .forEach(action -> Client.logger.info("Azione: " + action.toString()));
    }

    class ConcreteEventManager extends EventsAdapter {
        private ActionManager actionManager;

        public ConcreteEventManager() {
            actionManager = new ConcreteActionManager(actionBoard);
        }

        @Override
        public void process(ServerClosedEvent event) {
            Client.getInstance().close();
        }

        @Override
        public void process(PlayerTurnEvent event) {
            logAvailableActions(event);
            //PlayerBoard playerBoard = pokerTable.getPlayerBoardBy(event.getPlayerNickname());
            //playerBoard.activateColorTransition();
            if (event.getPlayerNickname().equalsIgnoreCase(Client.getInstance().getNickname()))
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
            playerBoardLogged = new PlayerBoard(event.getNickname(), event.getPosition(), true, event.getChips(), event.getAvatar());
            if (event.getNickname().equalsIgnoreCase(Client.getInstance().getNickname())) {
                playerBoardLogged.setNicknameColor(Color.YELLOW);
            }
            pokerTable.sit(playerBoardLogged);

        }

        @Override
        public void process(PlayerHasWinEvent event) {
            pokerTable.removePlayer(pokerTable.getPlayerBoardBy(event.getNickname()));
            if (Client.getInstance().getNickname().equals(event.getNickname())) {
                WinnerDialog dialog = new WinnerDialog("Hai vinto mentekatto", "Complimenti Player!! " +
                        event.getNickname());
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                dispose();
            }
        }

        @Override
        public void process(MatchLostEvent event) {
            if (Client.getInstance().getNickname().equals(event.getNickname())) {
                LoserDialog dialog = new LoserDialog("Hai perso!", "Ti sei classificato: " + event.getRankPosition() + "°. " +
                        "Vuoi continuare a seguire il match?", event.isCreator());
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
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

            if (event.getNickname().equalsIgnoreCase(Client.getInstance().getNickname()))
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
    }
}
