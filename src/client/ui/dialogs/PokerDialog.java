package client.ui.dialogs;

import client.event.MatchMode;
import client.event.PlayerConnected;
import client.net.ClientWrapper;
import client.net.LoggingTask;
import client.net.SendEventTask;
import client.ui.components.ActionButton;
import client.ui.components.Avatar;
import client.ui.components.PokerTextField;
import client.ui.frames.SelectAvatarFrame;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PokerDialog extends JDialog {
    private final static String INSERT_NICKNAME = "Inserisci nickname";
    private final static String CONNECT_TO_A_ROOM = "Inserisci IP stanza";
    private final static String DIALOG_TITLE = "Configurazione del match";
    private final static String POSITIVE_RESPONSE = "OK";
    private final static String NEGATIVE_RESPONSE = "CANCEL";

    private JPanel bgPanel;
    private JPanel responsePanel = new JPanel();
    private ActionButton confirmAction;
    private ActionButton cancelAction;
    private PokerTextField nicknameField;
    private PokerTextField ipAddress;
    private Color bgColor = new Color(0, 140, 65);

    public PokerDialog(Avatar avatar, boolean fixedLimitMode){
        createGUI();
        ipAddress.setEnabled(false);
        ipAddress.setText(Utils.getIpAddress());
        addConfirmButtonListener(event -> {
            if((!nicknameField.getText().equals(Utils.EMPTY) && !nicknameField.getText().equals(INSERT_NICKNAME.toUpperCase()))) {
                if(connectToServer()) {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(new SendEventTask(new MatchMode(fixedLimitMode)));
                    executor.submit(new SendEventTask(new PlayerConnected(nicknameField.getText(),
                            avatar.getName() + ".png")));
                    LoggingTask task = new LoggingTask(this, true, nicknameField.getText());
                    task.execute();
                }
            }
        });

        addCancelButtonListener(event -> {
            new SelectAvatarFrame(1);
            dispose();
        });
    }

    public PokerDialog(Avatar avatar) {
        createGUI();
        System.out.println("IP: " + Utils.getIpAddress());
        addConfirmButtonListener(event -> {
            if((!nicknameField.getText().equals(Utils.EMPTY) && !nicknameField.getText().equals(INSERT_NICKNAME.toUpperCase()))) {
                if (connectToServer()) {
                    new SendEventTask(new PlayerConnected(nicknameField.getText(), avatar.getName() + ".png")).execute();
                    LoggingTask task = new LoggingTask(this, false, nicknameField.getText());
                    task.execute();
                }
            }
        });

        addCancelButtonListener(event -> {
            new SelectAvatarFrame(2);
            dispose();
        });
    }

    private void createGUI(){
        setTitle(DIALOG_TITLE);

        createBgPanel();
        setBgPanelProperties();

        setResponsePanelProperties();

        createConfirmButton();
        setConfirmButtonProperties();
        attachConfirmButton();

        createCancelButton();
        setCancelButtonProperties();
        attachCancelButton();

        createNicknameTextField();
        attachNickname();

        createAddressTextField();
        attachAddress();

        attachResponsePanel();
        add(bgPanel);
    }

    private boolean connectToServer(){
        ClientWrapper client = ClientWrapper.getInstance();

        client.setNickname(nicknameField.getText());
        client.setParameters(ipAddress.getText(), 4040);
        return client.attemptToConnect();
    }

    private void createBgPanel(){
        bgPanel = new JPanel(new GridBagLayout());
    }

    private void setBgPanelProperties(){
        bgPanel.setBackground(bgColor);
    }

    private void createConfirmButton() {
        confirmAction = new ActionButton(POSITIVE_RESPONSE, Color.WHITE, 18);
    }

    private void createCancelButton() {
        cancelAction = new ActionButton(NEGATIVE_RESPONSE, Color.WHITE, 18);
    }

    private void createNicknameTextField() {
        nicknameField = new PokerTextField(INSERT_NICKNAME);
    }

    private void attachNickname() {
        bgPanel.add(nicknameField, new GBC(0, 0, 1, 1, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));
    }

    private void createAddressTextField() {
        ipAddress = new PokerTextField(CONNECT_TO_A_ROOM);
    }

    private void attachAddress(){
        bgPanel.add(ipAddress, new GBC(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));
    }

    private void setResponsePanelProperties(){
        responsePanel.setBackground(bgColor);
    }

    private void setConfirmButtonProperties() {
        confirmAction.setForeground(Color.BLACK);
    }

    private void setCancelButtonProperties() {
        cancelAction.setForeground(Color.BLACK);
    }

    private void attachConfirmButton(){
        responsePanel.add(confirmAction);
    }

    private void attachCancelButton(){
        responsePanel.add(cancelAction);
    }

    public void addConfirmButtonListener(ActionListener actionListener) {
        confirmAction.addActionListener(actionListener);
    }

    public void addCancelButtonListener(ActionListener actionListener) {
        cancelAction.addActionListener(actionListener);
    }

    private void attachResponsePanel(){
        bgPanel.add(responsePanel, new GBC(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));

    }

    public void setFocusOnButton() {
        confirmAction.requestFocusInWindow();
    }
}
