package client.ui.table;

import client.ui.components.Card;
import client.ui.components.PotLabel;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

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
    private PotLabel pot;
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
        //setBorder(new EmptyBorder(10, 10, 10, 10));
        //setBorder(new ThreeDimensionalBorder(Color.GREEN, 200, 3));
    }

    private void createPlate() {
        pot = new PotLabel(CENTER);
    }

    public void updatePotValue(int value){
        pot.appendPotValue(value);
    }

    private void setPlateProperties() {
        pot.setAlignmentX(CENTER_ALIGNMENT);
    }

    private void createCardsContainer() {
        cardsContainer = new JPanel();
    }

    private void setCardsContainerProperties() {
        cardsContainer.setLayout(new BoxLayout(cardsContainer, X_AXIS));
        cardsContainer.setAlignmentX(CENTER_ALIGNMENT);
        cardsContainer.setOpaque(false);
        //cardsContainer.setBackground(new Color(0, 80, 5));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g.create());
        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setRenderingHints(Utils.getHighQualityRenderingHints());
        g2D.setColor(new Color(0,70 , 20));
        Paint oldPaint = g2D.getPaint();
        LinearGradientPaint gradient = new LinearGradientPaint(0, 0, getWidth() - 4, getHeight() - 6,
                new float[]{0.0f, 0.5f, 1f}, new Color[]{new Color(0,70 , 20), new Color(0, 90, 20), new Color(0, 70, 20)});

        g2D.setColor(new Color(0, 60, 5));
        g2D.fillRoundRect(2,2, getWidth() - 2, getHeight() - 4, 60, 60);
        g2D.setPaint(gradient);
        g2D.fillRoundRect(4,4, getWidth() - 6, getHeight() - 8, 60, 60);

        g2D.setPaint(oldPaint);

    }

    private void attachPlate() {
        add(Box.createVerticalStrut(20));
        add(pot);
        add(Box.createVerticalStrut(20));
    }

    private void attachVerticalSeparator() {
        add(Box.createVerticalGlue());
    }

    private void attachCardsContainer() {
        add(cardsContainer);
    }

    public void createCards() {
        IntStream.range(0, 5).forEach($ -> cards.add(Card.createEmptyCard()));

    }

    public void removeCards(){
        cards.forEach(card -> cardsContainer.remove(card));
        cards.clear();
    }

    public void attachCards() {
        cards.forEach(this::addCard);
        add(Box.createVerticalStrut(25));
    }

    private void addCard(Card card){
        cardsContainer.add(card);
        cardsContainer.add(Box.createHorizontalStrut(2));
    }
    public void updateCard(String imageDirectoryPath) {
        Card card = cards.stream().filter(Card::isDefault).findFirst().get();
        card.setFrontImageDirectoryPath(imageDirectoryPath);
        card.loadImage();
        card.setVisible(true);
        repaint();
    }

    public void hideAllCards() {
        cards.forEach(card -> card.setVisible(false));
    }
}
