package graphics;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Un pannello che gestisce una sequenza ordinata di Carte da Poker.
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class CardsPanel extends JPanel {
    private ArrayList<CardView> cardsList;

    /**
     * Costruttore vuoto di CardsPanel.
     * Viene impostato come LayoutManager il BoxLayout, poichè più evoluto di FlowLayout e permette più controllo
     * sui figli.
     *
     * @param sizeLimit Limite massimo della lunghezza della sequenza.
     */

    public CardsPanel(int sizeLimit){
        cardsList = new ArrayList<>(sizeLimit);
        cardsList.trimToSize();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Utils.TRANSPARENT);
        add(Box.createHorizontalGlue());
    }

    /**
     * Permette di aggiungere una nuova carta alla sequenza.
     * @param card Prossima carta da aggiungere.
     */

    public void addNextCard(CardView card){
        card.setAlignmentX(CENTER_ALIGNMENT);
        cardsList.add(card);

        add(card);
        add(Box.createRigidArea(new Dimension(5,0)));

    }

    /**
     * Restituisce la CardView presente all'indice specificato della sequenza.
     * @param index Indice della sequenza.
     * @return CardView.
     */

    public CardView getCardView(int index){
        return cardsList.get(index);
    }

    /**
     * Restituisce la dimensione massima ammessa per il rendering del CardsPanel.
     * @return Dimensione massima ammessa.
     */

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(cardsList.get(0).getPreferredSize().width * cardsList.size(), cardsList.get(0).getPreferredSize().height* cardsList.size());
    }
}
