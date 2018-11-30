package client.ui.dialogs;


import client.ui.components.Avatar;
import client.ui.components.Card;
import client.ui.frames.Lobby;
import client.ui.frames.SelectAvatarFrame;
import server.controller.ServerManager;
import utils.GBC;
import utils.Utils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreatorDialog extends PokerDialog {
    private final static String PLAYERS = " PLAYERS";
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
            new Thread(new ServerManager(Integer.parseInt(numPlayersIndicator.getText().substring(0,1)))).start();
            new Lobby(nicknameField.getText(), avatar.getName() + ".png", Utils.getIpAddress());
            dispose();
        });

        addCancelButtonListener(event -> {
            new SelectAvatarFrame(0);
            dispose();
        });

        userResponse.add(confirmAction);

        userResponse.add(cancelAction);
        JPanel cardsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.X_AXIS));
        cardsPanel.setOpaque(false);
        Card card1 = Card.createCard(true, "backOrangePP.png");
        MouseListener mouseListener = new MouseListener(card1);
        card1.addMouseListener(mouseListener);
        cardsPanel.add(card1);
        cardsPanel.add(Card.createCard(true, "backRedPP.png"));
        cardsPanel.add(Card.createCard(true, "backBluePP.png"));
        container.add(cardsPanel, new GBC(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 40, 20, 20)));
        container.add(userResponse, new GBC(0, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 20, 20, 20)));
        add(container);
        confirmAction.requestFocus();
    }


    private void createLabelIndicator() {
        numPlayersIndicator = new JLabel(matchPlayers[0] + PLAYERS);
    }

    private void setLabelProperties() {
        numPlayersIndicator.setForeground(Color.WHITE);
        numPlayersIndicator.setBackground(Color.WHITE);
        numPlayersIndicator.setFont(new Font("helvetica", Font.BOLD, 30));
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
        leftArrow.addActionListener(event -> {
            int playerTemp = Integer.parseInt(numPlayersIndicator.getText().substring(0, 1));
            playerTemp = playerTemp - 1 < matchPlayers[0] ? matchPlayers[4] : playerTemp - 1;
            numPlayersIndicator.setText(playerTemp + PLAYERS);
        });
        rightArrow.setBackground(Color.WHITE);
        rightArrow.addActionListener(event -> {
            int playerTemp = Integer.parseInt(numPlayersIndicator.getText().substring(0, 1));
            playerTemp = playerTemp + 1 > matchPlayers[4] ? matchPlayers[0] : playerTemp + 1;
            numPlayersIndicator.setText(playerTemp + PLAYERS);
        });
    }

    private void attachIndicatorLogic() {
        indicatorContainer.add(leftArrow);
        indicatorContainer.add(numPlayersIndicator);
        indicatorContainer.add(rightArrow);
    }

    class MouseListener extends MouseAdapter{
        private Card card;
        public MouseListener(Card card){
            this.card = card;
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3, true));
        }
    }
}
