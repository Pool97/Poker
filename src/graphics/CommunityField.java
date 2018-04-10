package graphics;

import main.Card;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CommunityField extends JPanel {
    private ArrayList<Card> communityCards = new ArrayList<>(5);
    private JPanel cardsContainer;
    private JLabel tooltip = new JLabel("Community Cards", JLabel.CENTER);

    public CommunityField(){
        communityCards.trimToSize();
        setLayout(new BorderLayout());
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new FlowLayout());
        /*for(int i = 0; i < 5; i++){
            communityCards.add(new Card());
            cardsContainer.add(communityCards.get(i));
        }*/

        add(tooltip, BorderLayout.NORTH);
        add(cardsContainer, BorderLayout.CENTER);
    }
}
