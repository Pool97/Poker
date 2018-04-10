package graphics;

import main.Card;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class PlayerView extends JPanel {
    private JLabel surname;
    private JLabel action;
    private JLabel totalChips;
    private JLabel actualPosition;
    private ArrayList<Card> cards;
    //private avatarView
    private JLabel ranking;

    public PlayerView(String surname, String totalChips, String actualPosition, String action, String ranking){
        this.surname = new JLabel(surname);
        this.totalChips = new JLabel(totalChips);
        this.actualPosition = new JLabel(actualPosition);
        this.action = new JLabel(action);
        this.ranking = new JLabel(ranking);
        cards = new ArrayList<>();
        //cards.add(new Card("2", "fiori", 2));
        //cards.add(new Card("2", "fiori", 2));
        setLayout(new FlowLayout());
        JButton placeHolder = new JButton("Avatar");
        placeHolder.setPreferredSize(new Dimension(100, 150));
        add(placeHolder);
        JPanel container = new JPanel();
        container.setPreferredSize(new Dimension(200, 150));
        GridBagLayout infoContainer = new GridBagLayout();
        container.setLayout(infoContainer);
        GBC playerCards = new GBC(0, 0, 1, 1, 100, 100);
        playerCards.setAnchor(GBC.NORTHWEST);
        GBC rankingConstr = new GBC(1, 0, 1, 1,100, 100);
        rankingConstr.setAnchor(GBC.NORTHEAST);
        GBC surnameConstr = new GBC(0,1, 1, 1, 10, 10);
        surnameConstr.setAnchor(GBC.SOUTHWEST);
        GBC positionConstr = new GBC(1, 1, 1, 1, 10, 10);
        positionConstr.setAnchor(GBC.SOUTHEAST);
        GBC actionConstr = new GBC(0,2, 1, 1, 10, 10);
        actionConstr.setAnchor(GBC.SOUTHWEST);
        GBC totalChipsConstr = new GBC(1, 2, 1, 1, 10, 10);
        totalChipsConstr.setAnchor(GBC.SOUTHEAST);
        JPanel cards = new JPanel();
        cards.setLayout(new FlowLayout());
        cards.add(this.cards.get(0));
        cards.add(this.cards.get(1));
        container.add(cards, playerCards);
        container.add(this.ranking, rankingConstr);
        container.add(this.surname, surnameConstr);
        container.add(this.actualPosition, positionConstr);
        container.add(this.action, actionConstr);
        container.add(this.totalChips, totalChipsConstr);
        add(container);
    }
}
