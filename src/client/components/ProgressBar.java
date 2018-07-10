package client.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

public class ProgressBar extends JPanel {
    private int progressValue;

    public void updateValue(int progressValue) {
        this.progressValue = progressValue;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        setBackground(new Color(67, 160, 71));
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHints(Utils.getHighQualityRenderingHints());
        g2D.translate(this.getWidth() / 2, this.getHeight() / 2);
        g2D.rotate(Math.toRadians(270));
        Arc2D.Float arc = new Arc2D.Float(Arc2D.PIE);
        Ellipse2D circle = new Ellipse2D.Float(0, 0, 110 / 2, 110 / 2);
        arc.setFrameFromCenter(new Point(0, 0), new Point(120 / 2, 120 / 2));
        circle.setFrameFromCenter(new Point(0, 0), new Point(110 / 2, 110 / 2));
        arc.setAngleStart(1);
        arc.setAngleExtent(-progressValue * 3.6);
        g2D.setColor(Color.WHITE);
        g2D.draw(arc);
        g2D.fill(arc);
        g2D.setColor(new Color(67, 160, 71));
        g2D.draw(circle);
        g2D.fill(circle);
    }
}
