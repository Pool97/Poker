package client.components;

import utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static java.awt.Color.WHITE;
import static java.awt.Font.BOLD;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingConstants.CENTER;
import static utils.Utils.TRANSPARENT;

/**
 * View che rappresenta e gestisce le Community Cards del Poker.
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class CommunityCardsBoard extends JPanel {
    private final static String NAMEPLATE = "Community Cards";
    private JLabel plate;
    private JPanel cardsContainer;

    private CommunityCardsBoard() {
        setComponentProperties();

        createPlate();
        setPlateProperties();

        createCardsContainer();
        setCardsContainerProperties();

        attachPlate();
        attachVerticalSeparator();
        attachCardsContainer();
    }

    public static CommunityCardsBoard createEmptyCommunityCards() {
        return new CommunityCardsBoard();
    }

    private void setComponentProperties() {
        setLayout(new BoxLayout(this, Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(TRANSPARENT);
    }

    private void createPlate() {
        plate = new JLabel(NAMEPLATE, CENTER);
    }

    private void setPlateProperties() {
        plate.setForeground(WHITE);
        plate.setFont(Utils.getCustomFont(BOLD, 40F));
        plate.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void createCardsContainer() {
        cardsContainer = new JPanel();
    }

    private void setCardsContainerProperties() {
        cardsContainer.setLayout(new BoxLayout(cardsContainer, X_AXIS));
        cardsContainer.setAlignmentX(CENTER_ALIGNMENT);
        cardsContainer.setBackground(TRANSPARENT);
        cardsContainer.setOpaque(false);
    }

    private void attachPlate() {
        add(plate);
    }

    private void attachVerticalSeparator() {
        add(Box.createVerticalGlue());
    }

    private void attachCardsContainer() {
        add(cardsContainer);
    }

    public void addCard(Card nextCard) {
        nextCard.setAlignmentX(CENTER_ALIGNMENT);
        cardsContainer.add(nextCard);
    }
}
