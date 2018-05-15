package client.components;

import utils.GBC;
import utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MatchBoard extends JPanel {
    private JLabel smallBlind;
    private JLabel bigBlind;
    private JLabel pot;

    public MatchBoard(int smallBlind, int bigBlind, int pot) {
        setLayout(new GridBagLayout());
        setBackground(Utils.TRANSPARENT);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        this.smallBlind = new JLabel(Integer.toString(smallBlind));
        this.bigBlind = new JLabel(Integer.toString(bigBlind));
        this.pot = new JLabel(Integer.toString(pot));
        createGUI();
    }

    public void createGUI() {
        GBC gbc = new GBC(0, 0);
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 0, 10, 30);
        gbc.anchor = GBC.WEST;
        JLabel smallBlind = new JLabel("Small Blind");
        smallBlind.setFont(Utils.getCustomFont(Font.BOLD, 25));
        smallBlind.setForeground(Color.WHITE);
        add(smallBlind, gbc);

        GBC gbc1 = new GBC(1, 0);
        gbc1.weightx = 0.4;
        gbc1.insets = new Insets(0, 20, 10, 0);
        this.smallBlind.setFont(new Font("helvetica", Font.BOLD, 25));
        this.smallBlind.setForeground(Color.WHITE);
        add(this.smallBlind, gbc1);

        GBC gbc2 = new GBC(0, 1);
        gbc2.weightx = 0.6;
        gbc2.insets = new Insets(0, 0, 10, 20);
        gbc2.anchor = GBC.WEST;
        JLabel bigBlind = new JLabel("Big Blind");
        bigBlind.setFont(Utils.getCustomFont(Font.BOLD, 25));
        bigBlind.setForeground(Color.WHITE);
        add(bigBlind, gbc2);

        GBC gbc3 = new GBC(1, 1);
        gbc3.weightx = 0.4;
        gbc3.insets = new Insets(0, 20, 10, 0);
        this.bigBlind.setFont(new Font("helvetica", Font.BOLD, 25));
        this.bigBlind.setForeground(Color.WHITE);
        add(this.bigBlind, gbc3);

        GBC gbc4 = new GBC(0, 2);
        gbc4.weightx = 0.6;
        gbc4.insets = new Insets(0, 0, 0, 20);
        gbc4.anchor = GBC.WEST;
        JLabel pot = new JLabel("Pot");
        pot.setFont(Utils.getCustomFont(Font.BOLD, 25));
        pot.setForeground(Color.WHITE);
        add(pot, gbc4);

        GBC gbc5 = new GBC(1, 2);
        gbc5.weightx = 0.4;
        gbc5.insets = new Insets(0, 20, 0, 0);
        this.pot.setFont(new Font("helvetica", Font.BOLD, 25));
        this.pot.setForeground(Color.WHITE);
        add(this.pot, gbc5);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2D.setRenderingHints(qualityHints);


        g2D.setColor(Color.BLACK);
        g2D.fillRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 30, 30);


        g2D.setStroke(new BasicStroke(4));
        g2D.setColor(new Color(255, 215, 0));
        g2D.drawRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 30, 30);
    }

    public void setSmallBlind(int smallBlind) {
        this.smallBlind.setText(Integer.toString(smallBlind));
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind.setText(Integer.toString(bigBlind));
    }

    public void setPot(int pot) {
        this.pot.setText(Integer.toString(pot));
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
