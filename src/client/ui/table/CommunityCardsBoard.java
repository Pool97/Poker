package client.ui.table;

import client.ui.components.Card;
import client.ui.components.PotLabel;
import utils.Utils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingConstants.CENTER;

public class CommunityCardsBoard extends JPanel {
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

        createCardPlaceholders();

        attachPlate();
        attachCardsContainer();
        attachCards();

        add(Box.createVerticalStrut(25));
    }

    public static CommunityCardsBoard createEmptyCommunityCards() {
        return new CommunityCardsBoard();
    }

    private void setComponentProperties() {
        setLayout(new BoxLayout(this, Y_AXIS));
        setBackground(Utils.TRANSPARENT);
        setOpaque(false);
    }

    private void createPlate() {
        pot = new PotLabel(CENTER);
    }

    public void updatePot(int value){
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
    }

    private void attachPlate() {
        add(Box.createVerticalStrut(20));
        add(pot);
        add(Box.createVerticalStrut(20));
    }

    private void attachCardsContainer() {
        add(cardsContainer);
    }

    public void createCardPlaceholders() {
        IntStream.range(0, 5).forEach($ -> cards.add(Card.createPlaceholder()));
        attachCards();
    }

    public void clearBoard(){
        cards.forEach(card -> cardsContainer.remove(card));
        cards.clear();
    }

    public void revealCard(String imagePath) {
        Card card = cards.stream().filter(Card::isPlaceHolder).findFirst().get();
        card.setFrontImageDirectoryPath(imagePath);
    }

    private void attachCards() {
        cards.forEach(this::addCard);

    }

    private void addCard(Card card){
        cardsContainer.add(card);
        cardsContainer.add(Box.createHorizontalStrut(2));
    }
}
