package client.view;

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

public class CommunityField extends JPanel {
    private CardsPanel cardsPanel;
    private JLabel tooltip = new JLabel("Community Cards", JLabel.CENTER);

    /**
     * Costruttore vuoto del CommunityField.
     */

    public CommunityField(Dimension cardsSize){
        cardsPanel = new CardsPanel(cardsSize, 5);
        initView();
    }

    /**
     * Inizializzazione della View.
     */

    private void initView(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Utils.TRANSPARENT);
        tooltip.setForeground(Color.WHITE);
        tooltip.setFont(Utils.getCustomFont(Font.BOLD, 40F));
        tooltip.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardsPanel.setAlignmentX(CENTER_ALIGNMENT);
        cardsPanel.setAlignmentY(CENTER_ALIGNMENT);
        add(tooltip);
        add(cardsPanel);
    }

    /**
     * Permette di aggiungere al CommunityField una nuova carta. Tipicamente questa azione avviene durante il Flop,
     * il Turn o il River.
     * @param nextCard Prossima carta da aggiungere alle Community Cards.
     */

    public void addNextCard(CardView nextCard){
        cardsPanel.addNextCard(nextCard);
    }

}
