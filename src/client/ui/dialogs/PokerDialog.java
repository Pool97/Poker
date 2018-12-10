package client.ui.dialogs;

import client.events.MatchMode;
import client.events.PlayerConnected;
import client.net.Client;
import client.ui.components.Avatar;
import client.ui.components.PokerTextField;
import client.ui.frames.Lobby;
import client.ui.frames.SelectAvatarFrame;
import client.ui.userboard.ActionButton;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
        System.out.println("IP: " + Utils.getIpAddress());
        addConfirmButtonListener(event -> {
            connectToServer();
            Client.getInstance().writeMessage(new MatchMode(fixedLimitMode));
            Client.getInstance().writeMessage(new PlayerConnected(nicknameField.getText(),
                    avatar.getName() + ".png"));
            openLobby();
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
            connectToServer();
            Client.getInstance().writeMessage(new PlayerConnected(nicknameField.getText(), avatar.getName() + ".png"));
            openLobby();
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

    private void connectToServer(){
        Client client = Client.getInstance();

        client.setNickname(nicknameField.getText());
        client.setParameters(ipAddress.getText(), 4040);
        client.attemptToConnect();
    }

    private void openLobby(){
        new Lobby(ipAddress.getText());
        dispose();
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
