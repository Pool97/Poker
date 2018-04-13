package graphics;

import utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class CommunityField extends JPanel {
    private ArrayList<CardView> communityCards;
    private JPanel cardsContainer;
    private JLabel tooltip = new JLabel("Community Cards", JLabel.CENTER);

    public CommunityField(){
        communityCards = new ArrayList<>(5);
        communityCards.trimToSize();

        initView();
        setOpaque(true);
        tooltip.setForeground(Color.WHITE);
        tooltip.setFont(Utils.getCustomFont(Font.BOLD, 40F));
        tooltip.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardsContainer = new JPanel();
        cardsContainer.setLayout(new FlowLayout());
        cardsContainer.setBackground(new Color(0,0,0,0));

        add(tooltip, BorderLayout.NORTH);
        add(cardsContainer, BorderLayout.CENTER);
    }

    private void initView(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(0,0,0,0));
    }

    public void addNextCard(CardView nextCard){
        communityCards.add(nextCard);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2.0F));
        g2.setColor(Color.WHITE);
        g2.drawRect(1,1, getWidth() - 2, getHeight() - 2);
    }
}
