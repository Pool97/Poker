package client.components;

import javax.swing.*;
import java.awt.*;

import static utils.Utils.getHighQualityRenderingHints;

public abstract class BorderPanel extends JPanel {
    protected Color borderColor;

    public BorderPanel() {
        borderColor = Color.WHITE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHints(getHighQualityRenderingHints());
        drawBackground(g2D);
        drawBorder(g2D, borderColor);
    }

    protected abstract void drawBackground(Graphics2D g2D);

    protected abstract void drawBorder(Graphics2D g2D, Color color);
}
