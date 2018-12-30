package client.ui.frames;

import client.events.ConcreteEventManager;
import client.net.ClientWrapper;
import client.net.ReadServerMessagesTask;
import client.ui.components.Chat;
import client.ui.components.GameBoard;
import client.ui.table.PokerTable;
import client.ui.userboard.ActionBoard;
import server.events.ChatNotify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BoardFrame extends JFrame {
    private GameBoard gameBoard;
    private PokerTable pokerTable;
    private ActionBoard actionBoard;
    private Chat chat;
    private ReadServerMessagesTask readTask;
    private String nickname;

    public BoardFrame(String nickname) {
        this.nickname = nickname;
        createBoard();
        createChat();
        createPokerTable();
        createUserBoard();

        attachComponents();
        setFrameProperties();
        listenToServer();
    }

    private void createChat(){
        chat = new Chat(Toolkit.getDefaultToolkit().getScreenSize().width / 5, nickname);
        chat.addNotify(new ChatNotify("Benvenuto in Poker Texas!"));
    }

    private void createBoard() {
        gameBoard = new GameBoard();
    }

    private void createPokerTable() {
        pokerTable = new PokerTable();
    }

    private void createUserBoard() {
        actionBoard = new ActionBoard();
    }

    private void attachComponents() {
        attachPokerTable();
        attachUserBoard();
        attachBoard();
        attachChat();
    }

    private void attachPokerTable() {
        gameBoard.attach(pokerTable);
    }

    private void attachChat(){
        gameBoard.attachChat(chat);
    }

    private void attachUserBoard() {
        gameBoard.attach(actionBoard);
    }

    private void attachBoard() {
        add(gameBoard);
    }

    private void setFrameProperties() {
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                readTask.stopTask();
                ClientWrapper.getInstance().close();
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void listenToServer() {
        readTask = new ReadServerMessagesTask();
        readTask.setEventsManager(new ConcreteEventManager(this));
        readTask.execute();
    }

    public PokerTable getPokerTable(){
        return pokerTable;
    }

    public ActionBoard getActionBoard(){
        return actionBoard;
    }

    public Chat getChat(){
        return chat;
    }
}
