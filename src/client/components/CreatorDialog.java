package client.components;


import client.frames.CreatorGameFrame;
import client.frames.SelectAvatarFrame;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;

public class CreatorDialog extends PokerDialog {
    private final static String DEFAULT_PLAYERS_NUMBER = "2 PLAYERS";
    private static Color bgColor = new Color(255, 126, 5);
    private int[] matchPlayers = {2, 3, 4, 5, 6};
    private JPanel container;
    private JLabel numPlayersIndicator;
    private JPanel indicatorContainer;
    private BasicArrowButton leftArrow;
    private BasicArrowButton rightArrow;

    public CreatorDialog(Avatar avatar) {
        super();
        container = new JPanel(new GridBagLayout());
        container.setBackground(bgColor);
        attachNickname();

        createLabelIndicator();
        setLabelProperties();

        indicatorContainer = new JPanel();
        indicatorContainer.setBackground(bgColor);

        createArrowButtons();
        setArrowButtonsProperties();
        attachIndicatorLogic();

        container.add(indicatorContainer, new GBC(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 0, 20, 0)));

        JPanel userResponse = new JPanel();

        userResponse.setBackground(bgColor);

        addConfirmButtonListener(event -> {
            new CreatorGameFrame(nicknameField.getText(), avatar.getName() + ".png", Integer.parseInt(numPlayersIndicator.getText().substring(0, 1)), Utils.getIpAddress());
            dispose();
        });

        addCancelButtonListener(event -> {
            new SelectAvatarFrame(1);
            dispose();
        });

        userResponse.add(confirmAction);
        userResponse.add(cancelAction);
        container.add(userResponse, new GBC(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));
        container.requestFocusInWindow();
        add(container);
    }


    private void createLabelIndicator() {
        numPlayersIndicator = new JLabel(DEFAULT_PLAYERS_NUMBER);
    }

    private void setLabelProperties() {
        numPlayersIndicator.setForeground(Color.WHITE);
        numPlayersIndicator.setBackground(Color.WHITE);
        numPlayersIndicator.setFont(new Font("helvetica", Font.BOLD, 26));
        numPlayersIndicator.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    }

    private void attachNickname() {
        container.add(nicknameField, new GBC(0, 0, 1, 1, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));
    }

    private void createArrowButtons() {
        leftArrow = new BasicArrowButton(BasicArrowButton.WEST);
        rightArrow = new BasicArrowButton(BasicArrowButton.EAST);
    }

    private void setArrowButtonsProperties() {
        leftArrow.setBackground(Color.WHITE);
        leftArrow.addActionListener(event -> numPlayersIndicator.setText((Integer.parseInt(numPlayersIndicator.getText().substring(0, 1)) - 1) + " " + "PLAYERS"));
        rightArrow.setBackground(Color.WHITE);
        rightArrow.addActionListener(event -> {
            numPlayersIndicator.setText((Integer.parseInt(numPlayersIndicator.getText().substring(0, 1)) + 1) + " " + "PLAYERS");
        });
    }

    private void attachIndicatorLogic() {
        indicatorContainer.add(leftArrow);
        indicatorContainer.add(numPlayersIndicator);
        indicatorContainer.add(rightArrow);
    }
}
