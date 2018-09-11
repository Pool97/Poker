package client.ui.components;

import utils.GBC;

import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.WEST;
import static utils.Utils.TRANSPARENT;

public class MatchBoard extends BorderPanel {
    private final static String SMALL_BLIND_DESCR = "Small Blind";
    private final static String BIG_BLIND_DESCR = "Big Blind";
    private final static String POT_DESCR = "Pot";
    private final static int INITIAL_VALUE = 0;
    private EntryPanel smallBlind;
    private EntryPanel bigBlind;
    private EntryPanel pot;

    public MatchBoard() {
        setComponentProperties();

        createSmallBlind();
        attachSmallBlind();

        createBigBlind();
        attachBigBlind();

        createPot();
        attachPot();
    }

    private void setComponentProperties() {
        setLayout(new GridBagLayout());
        setBackground(TRANSPARENT);
        setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    private void createSmallBlind() {
        smallBlind = EntryPanel.createWith(SMALL_BLIND_DESCR, INITIAL_VALUE);
    }

    private void attachSmallBlind() {
        GBC gbc = new GBC(0, 0, 1, 1, 1, 1, WEST, HORIZONTAL, new Insets(0, 0, 10, 10));
        add(smallBlind, gbc);
    }

    private void createBigBlind() {
        bigBlind = EntryPanel.createWith(BIG_BLIND_DESCR, INITIAL_VALUE);
    }

    private void attachBigBlind() {
        GBC gbc = new GBC(0, 1, 0.6, 1, 1, 1, WEST, HORIZONTAL, new Insets(0, 0, 10, 10));
        add(bigBlind, gbc);
    }

    private void createPot() {
        pot = EntryPanel.createWith(POT_DESCR, INITIAL_VALUE);
    }

    private void attachPot() {
        GBC gbc = new GBC(0, 2, 0.6, 1, 1, 1, WEST, HORIZONTAL, new Insets(0, 0, 0, 10));
        add(pot, gbc);
    }

    public void setSmallBlind(int value) {
        smallBlind.setValue(value);
    }

    public void setBigBlind(int value) {
        bigBlind.setValue(value);
    }

    public void setPot(int value) {
        pot.setValue(value);
    }

    public void drawBackground(Graphics2D g2D) {
        g2D.setColor(new Color(20, 52, 204));
        g2D.fillRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 30, 30);
    }

    public void drawBorder(Graphics2D g2D, Color color) {
        g2D.setStroke(new BasicStroke(4));
        g2D.setColor(Color.WHITE);
        g2D.drawRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 30, 30);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(350, 150);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(350, 150);
    }
}
