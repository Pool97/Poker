package client.ui.components;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.WHITE;
import static utils.Utils.getHighQualityRenderingHints;

public abstract class BorderPanel extends JPanel {
    protected Color borderColor;

    /**
     * Stroke size. it is recommended to set it to 1 for better view
     */
    protected int strokeSize = 3;
    /**
     * Color of shadow
     */
    protected Color shadowColor = new Color(0, 40, 5);
    /**
     * Sets if it drops shadow
     */
    protected boolean shady = true;
    /**
     * Sets if it has an High Quality view
     */
    protected boolean highQuality = true;
    /**
     * Double values for Horizontal and Vertical radius of corner arcs
     */
    protected Dimension arcs = new Dimension(40, 40);
    //protected Dimension arcs = new Dimension(20, 20);//creates curved borders and panel
    /**
     * Distance between shadow border and opaque panel border
     */
    protected int shadowGap = 8;
    /**
     * The offset of shadow.
     */
    protected int shadowOffset = 7;
    /**
     * The transparency value of shadow. ( 0 - 255)
     */
    protected int shadowAlpha = 180;


    public BorderPanel() {
        borderColor = Color.WHITE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color shadowColorA = new Color(shadowColor.getRed(),
                shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
        Graphics2D graphics = (Graphics2D) g;
        if(highQuality)
            graphics.setRenderingHints(getHighQualityRenderingHints());

        //Draws shadow borders if any.
        if (shady) {
            graphics.setColor(shadowColorA);
            graphics.fillRoundRect(
                    shadowOffset,// X position
                    shadowOffset,// Y position
                    getWidth() - strokeSize - shadowOffset, // width
                    getHeight() - strokeSize - shadowOffset, // height
                    arcs.width, arcs.height);// arc Dimension
        } else {
            shadowGap = 1;
        }

        Paint paint = graphics.getPaint();
        LinearGradientPaint gradient = new LinearGradientPaint(strokeSize, strokeSize, getWidth()- shadowGap - strokeSize, getHeight()-shadowGap-strokeSize,
                new float[]{0.0f, 0.5f, 1f}, new Color[]{new Color(0, 130, 178), new Color(0, 115, 178), new Color(0, 100, 178)});
        graphics.setPaint(gradient);
        //Draws the rounded opaque panel with borders.
        graphics.fillRoundRect(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                getHeight() - shadowGap - strokeSize, arcs.width, arcs.height);
        graphics.setPaint(paint);
        graphics.setColor(WHITE);
        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.drawRoundRect(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                getHeight() - shadowGap - strokeSize, arcs.width, arcs.height);
    }

    protected abstract void drawBackground(Graphics2D g2D);

    protected abstract void drawBorder(Graphics2D g2D, Color color);
}
