package client.ui.components;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class ThreeDimensionalBorder extends AbstractBorder {

    private static final long serialVersionUID = 1L;
    private Color color;
    private int thickness = 4;
    private int radii = 5;
    private Insets insets = null;
    private BasicStroke stroke = null;
    private int strokePad;
    RenderingHints hints;
    int shadowPad = 3;

    public ThreeDimensionalBorder(Color color) {
        this(color, 128, 8);
    }

    public ThreeDimensionalBorder(Color color, int transparency, int shadowWidth) {
        this.color = color;
        shadowPad = shadowWidth;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + strokePad;
        int bottomPad = strokePad + shadowPad;
        int rightPad = strokePad + shadowPad;
        insets = new Insets(pad, pad, bottomPad + shadowPad, rightPad);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        System.out.println("PROVA" + insets.toString());
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(
            Component c,
            Graphics g,
            int x, int y,
            int width, int height) {

        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - thickness - shadowPad;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
                0 + strokePad,
                0 + strokePad,
                width - thickness - shadowPad - 6,
                bottomLineY,
                radii,
                radii
        );

        Area area = new Area(bubble);

        g2.setRenderingHints(hints);

        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);

        Area shadowArea = new Area(new Rectangle(0, 0, width, height));
        area.subtract(shadowArea);
        g.setClip(shadowArea);
        Color shadow = new Color(color.getRed(), color.getGreen(), color.getBlue(), 128);
        g2.setColor(shadow);
        g2.translate(shadowPad, shadowPad);
        g2.draw(area);
    }
}

