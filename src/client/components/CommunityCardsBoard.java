package client.components;

import utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static java.awt.Color.WHITE;
import static java.awt.Font.BOLD;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingConstants.CENTER;

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
    private ArrayList<Card> cards;

    private CommunityCardsBoard() {
        cards = new ArrayList<>();
        setComponentProperties();

        createPlate();
        setPlateProperties();

        createCardsContainer();
        setCardsContainerProperties();

        createCards();
        attachPlate();
        //attachVerticalSeparator();
        attachCardsContainer();
        attachCards();
    }

    public static CommunityCardsBoard createEmptyCommunityCards() {
        return new CommunityCardsBoard();
    }

    private void setComponentProperties() {
        setLayout(new BoxLayout(this, Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(0, 80, 5));
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
        cardsContainer.setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        cardsContainer.setBackground(new Color(0, 80, 5));
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

    private void createCards() {
        IntStream.range(0, 5).forEach($ -> cards.add(Card.createEmptyCard()));
    }

    private void attachCards() {
        cards.forEach(card -> cardsContainer.add(card));
    }

    public void updateCard(String imageDirectoryPath) {
        Card card = cards.stream().filter(communityCard -> !communityCard.isVisible()).findFirst().get();
        card.setFrontImageDirectoryPath(imageDirectoryPath);
        card.loadImage();
        card.setVisible(true);
        repaint();
    }

    public void hideAllCards() {
        cards.forEach(card -> card.setVisible(false));
    }
}
