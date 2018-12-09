package client.ui.components;

import javax.swing.*;
import java.awt.*;

public class PotLabel extends JLabel {
    public PotLabel(String text, int horizontalAlignment) {
        super("POT", horizontalAlignment);
        setFont(new Font("helvetica", Font.BOLD, 24));
        setForeground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D)g;
        Composite oldComposite = graphics2D.getComposite();
        AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.60f);
        graphics2D.setComposite(newComposite);
        graphics2D.setColor(new Color(0, 80, 5));
        graphics2D.fillRoundRect(3,3, getWidth() - 6, getHeight()- 6, 30, 30);
        graphics2D.setComposite(oldComposite);
        graphics2D.setColor(new Color(0, 60, 5));
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawRoundRect(2,2, getWidth() - 4, getHeight() - 4, 30, 30);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(100, 30);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(200, 30);
    }
}
