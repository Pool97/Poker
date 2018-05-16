package client.components;

import utils.GBC;
import utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.Color.BLACK;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.WEST;
import static utils.Utils.TRANSPARENT;

public class MatchBoard extends JPanel {
    private final static String SMALL_BLIND_DESCR = "Small Blind";
    private final static String BIG_BLIND_DESCR = "Big Blind";
    private final static String POT_DESCR = "Pot";
    private final static int INITIAL_VALUE = 0;
    private Entry smallBlind;
    private Entry bigBlind;
    private Entry pot;

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
        smallBlind = Entry.createWith(SMALL_BLIND_DESCR, INITIAL_VALUE);
    }

    private void attachSmallBlind() {
        GBC gbc = new GBC(0, 0, 1, 1, 1, 1, WEST, HORIZONTAL, new Insets(0, 0, 10, 10));
        add(smallBlind, gbc);
    }

    private void createBigBlind() {
        bigBlind = Entry.createWith(BIG_BLIND_DESCR, INITIAL_VALUE);
    }

    private void attachBigBlind() {
        GBC gbc = new GBC(0, 1, 0.6, 1, 1, 1, WEST, HORIZONTAL, new Insets(0, 0, 10, 10));
        add(bigBlind, gbc);
    }

    private void createPot() {
        pot = Entry.createWith(POT_DESCR, INITIAL_VALUE);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        g2D.setRenderingHints(Utils.getHighQualityRenderingHints());
        drawBackground(g2D);
        drawBorder(g2D);
    }

    public void drawBackground(Graphics2D g2D) {
        g2D.setColor(BLACK);
        g2D.fillRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 30, 30);
    }

    public void drawBorder(Graphics2D g2D) {
        g2D.setStroke(new BasicStroke(4));
        g2D.setColor(new Color(255, 215, 0));
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
