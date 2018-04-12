package graphics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CommunityField extends JPanel {
    private ArrayList<CardView> communityCards = new ArrayList<>(5);
    private JPanel cardsContainer;
    private JLabel tooltip = new JLabel("Community Cards", JLabel.CENTER);

    public CommunityField(){
        communityCards.trimToSize();
        setLayout(new BorderLayout());
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new FlowLayout());
        communityCards.add(new CardView(new Dimension(130, 180), "2_fiori.png"));
        communityCards.add(new CardView(new Dimension(130, 180), "2_quadri.png"));
        communityCards.add(new CardView(new Dimension(130, 180), "10_cuori.png"));
        communityCards.add(new CardView(new Dimension(130, 180), "asso_picche.png"));
        communityCards.add(new CardView(new Dimension(130, 180), "asso_fiori.png"));

        for(int i = 0; i < communityCards.size(); i++){
            cardsContainer.add(communityCards.get(i));
        }

        tooltip.setFont(new Font("Helvetica", Font.BOLD, 24));
        add(tooltip, BorderLayout.NORTH);
        add(cardsContainer, BorderLayout.CENTER);
    }
}
