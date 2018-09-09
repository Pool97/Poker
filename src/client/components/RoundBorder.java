package client.components;

import utils.Utils;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundBorder implements Border {

    private int radius;

    public RoundBorder(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHints(Utils.getHighQualityRenderingHints());
        g2d.setColor(Color.WHITE);
        g2d.draw(new RoundRectangle2D.Double(x + 5, y + 2, width - 10, height - 7, getRadius(), getRadius()));
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int value = getRadius() / 10;
        return new Insets(value, value, value, value);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

}
