package client.ui.dialogs;

import client.events.MatchModeEvent;
import client.events.PlayerConnectedEvent;
import client.net.Client;
import client.ui.components.Avatar;
import client.ui.components.PokerTextField;
import client.ui.frames.Lobby;
import client.ui.frames.SelectAvatarFrame;
import server.events.EventsContainer;
import utils.GBC;

import javax.swing.*;
import java.awt.*;

public class PlayerDialog extends PokerDialog {
    private final static String CONNECT_TO_A_ROOM = "Inserisci IP stanza";
    private JPanel container;
    private PokerTextField ipAddress;

    public PlayerDialog(Avatar avatar){
        super();
        container = new JPanel(new GridBagLayout());
        createAddressTextField();

        JPanel userResponse = new JPanel();
        addConfirmButtonListener(event -> {
            Client client = Client.getInstance();
            client.setNickname(nicknameField.getText());
            client.setParameters(ipAddress.getText(), 4040);
            client.attemptToConnect();
            Client.getInstance().writeMessage(new EventsContainer(new PlayerConnectedEvent(nicknameField.getText(), avatar.getName() + ".png")));
            new Lobby(ipAddress.getText());
            dispose();
        });

        addCancelButtonListener(event -> {
            new SelectAvatarFrame(2);
            dispose();
        });

        userResponse.add(confirmAction);
        userResponse.add(cancelAction);

        attachNickname();
        container.add(ipAddress, new GBC(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));
        container.add(userResponse, new GBC(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));
        add(container);
    }

    public PlayerDialog(Avatar avatar, boolean fixedLimitMode) {
        super();
        container = new JPanel(new GridBagLayout());
        createAddressTextField();

        JPanel userResponse = new JPanel();

        addConfirmButtonListener(event -> {
            Client client = Client.getInstance();
            client.setNickname(nicknameField.getText());
            client.setParameters(ipAddress.getText(), 4040);
            client.attemptToConnect();
            Client.getInstance().writeMessage(new EventsContainer(new MatchModeEvent(fixedLimitMode)));
            Client.getInstance().writeMessage(new EventsContainer(new PlayerConnectedEvent(nicknameField.getText(), avatar.getName() + ".png")));
            new Lobby(ipAddress.getText());
            dispose();
        });

        addCancelButtonListener(event -> {
            new SelectAvatarFrame(2);
            dispose();
        });

        userResponse.add(confirmAction);
        userResponse.add(cancelAction);

        attachNickname();
        container.add(ipAddress, new GBC(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));
        container.add(userResponse, new GBC(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));
        add(container);
    }

    private void attachNickname() {
        container.add(nicknameField, new GBC(0, 0, 1, 1, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));
    }


    private void createAddressTextField() {
        ipAddress = new PokerTextField(CONNECT_TO_A_ROOM);
    }
}
