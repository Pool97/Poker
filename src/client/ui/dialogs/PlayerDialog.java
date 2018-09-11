package client.ui.dialogs;

import client.ui.components.Avatar;
import client.ui.components.PokerTextField;
import client.ui.frames.GameFrame;
import client.ui.frames.SelectAvatarFrame;
import utils.GBC;

import javax.swing.*;
import java.awt.*;

public class PlayerDialog extends PokerDialog {
    private final static String CONNECT_TO_A_ROOM = "Inserisci IP stanza";
    private JPanel container;
    private PokerTextField ipAddress;

    public PlayerDialog(Avatar avatar) {
        super();
        container = new JPanel(new GridBagLayout());
        createAddressTextField();

        JPanel userResponse = new JPanel();

        addConfirmButtonListener(event -> {
            new GameFrame(nicknameField.getText(), avatar.getName() + ".png", ipAddress.getText());
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
