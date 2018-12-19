package client.ui.components;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.WHITE;
import static utils.Utils.getHighQualityRenderingHints;

public abstract class BorderPanel extends JPanel {
    protected Color borderColor;
    protected int strokeSize;
    protected Color shadowColor;
    protected boolean dropsShadow;
    protected boolean highQuality;
    protected Dimension arcs;
    protected int shadowGap;
    protected int shadowOffset;
    protected int shadowAlpha;

    public BorderPanel() {
        shadowColor = new Color(0, 40, 5);
        borderColor = Color.WHITE;
        arcs = new Dimension(40, 40);
        dropsShadow = true;
        highQuality = true;
        strokeSize = 3;
        shadowGap = 8;
        shadowOffset = 7;
        shadowAlpha = 180;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;

        if(highQuality)
            graphics.setRenderingHints(getHighQualityRenderingHints());

        if (dropsShadow)
            drawShadowBorder(graphics);
        else
            shadowGap = 1;

        Paint paint = graphics.getPaint();

        drawLinearGradient(graphics);

        graphics.setPaint(paint);
        graphics.setColor(WHITE);
        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.drawRoundRect(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                getHeight() - shadowGap - strokeSize, arcs.width, arcs.height);
    }

    private void drawShadowBorder(Graphics2D graphics){
        Color shadowColorA = new Color(shadowColor.getRed(),
                shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
        graphics.setColor(shadowColorA);
        graphics.fillRoundRect(
                shadowOffset,// X position
                shadowOffset,// Y position
                getWidth() - strokeSize - shadowOffset, // width
                getHeight() - strokeSize - shadowOffset, // height
                arcs.width, arcs.height);// arc Dimension
    }

    private void drawLinearGradient(Graphics2D graphics){
        LinearGradientPaint gradient = new LinearGradientPaint(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                getHeight()-shadowGap-strokeSize, new float[]{0.0f, 0.5f, 1f}, new Color[]{new Color(0, 130, 178),
                new Color(0, 115, 178), new Color(0, 100, 178)});
        graphics.setPaint(gradient);
        graphics.fillRoundRect(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                getHeight() - shadowGap - strokeSize, arcs.width, arcs.height);
    }

    public void setArcDimension(Dimension dimension){
        arcs = dimension;
        repaint();
    }

    public void setShadowColor(Color shadowColor){
        this.shadowColor = shadowColor;
    }

    public void dropsShadow(boolean dropsShadow){
        this.dropsShadow = dropsShadow;
    }

    public void setShadowGap(int shadowGap){
        this.shadowGap = shadowGap;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    public void setDropsShadow(boolean dropsShadow) {
        this.dropsShadow = dropsShadow;
    }

    public void setHighQuality(boolean highQuality) {
        this.highQuality = highQuality;
    }

    public void setArcs(Dimension arcs) {
        this.arcs = arcs;
    }

    public void setShadowOffset(int shadowOffset) {
        this.shadowOffset = shadowOffset;
    }

    public void setShadowAlpha(int shadowAlpha) {
        this.shadowAlpha = shadowAlpha;
    }
}
