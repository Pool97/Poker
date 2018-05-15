package client.components;

import utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * View che rappresenta e gestisce le Community Cards del Poker.
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class CommunityCardsBoard extends JPanel {
    private JLabel tooltip = new JLabel("Community Cards", JLabel.CENTER);
    private JPanel cardsPanel;

    /**
     * Costruttore vuoto del CommunityCardsBoard.
     */

    private CommunityCardsBoard() {
        cardsPanel = new JPanel();
        initView();
        /*for (int i = 0; i < 5; i++) {
            Card card = new Card(System.getProperty(Utils.WORKING_DIRECTORY) + Utils.RES_DIRECTORY + "3_cuori1.png", System.getProperty(Utils.WORKING_DIRECTORY) + Utils.RES_DIRECTORY + "back.png");
            card.setMaximumSize(new Dimension(200, 200));
            addNextCard(card);
        }*/
    }

    public static CommunityCardsBoard createEmptyCommunityCards() {
        return new CommunityCardsBoard();
    }

    /**
     * Inizializzazione della View.
     */

    private void initView(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.X_AXIS));
        cardsPanel.setAlignmentX(CENTER_ALIGNMENT);
        cardsPanel.setBackground(Utils.TRANSPARENT);
        cardsPanel.setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Utils.TRANSPARENT);
        tooltip.setForeground(Color.WHITE);
        tooltip.setFont(Utils.getCustomFont(Font.BOLD, 40F));
        tooltip.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(tooltip);
        add(Box.createVerticalGlue());
        add(cardsPanel);
    }

    /**
     * Permette di aggiungere al CommunityCardsBoard una nuova carta. Tipicamente questa azione avviene durante il Flop,
     * il Turn o il River.
     * @param nextCard Prossima carta da aggiungere alle Community Cards.
     */

    public void addNextCard(Card nextCard) {
        nextCard.setAlignmentX(CENTER_ALIGNMENT);
        cardsPanel.add(nextCard);
    }

    @Override
    public Dimension getMaximumSize() {
        return super.getMaximumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return super.getMinimumSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }
}
