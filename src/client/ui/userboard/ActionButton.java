package client.ui.userboard;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import static java.awt.Font.PLAIN;
import static utils.Utils.DEFAULT_FONT;

public class ActionButton extends JButton implements MouseListener {
    public float opacity;
    BufferedImage buttonImage = null;
    Color color;
    private float alpha;

    public ActionButton(String action, Color color) {
        super(action);
        this.color = color;
        setComponentProperties(30);
        setOpaque(false);
        addMouseListener(this);
        opacity = .0f;
    }

    public ActionButton(String action, Color color, int fontSize) {
        super(action);
        this.color = color;
        setComponentProperties(fontSize);
        setOpaque(false);
        addMouseListener(this);
        opacity = .0f;
    }

    private void setComponentProperties(int fontSize) {
        setFont(new Font(DEFAULT_FONT, PLAIN, fontSize));
        setForeground(Color.WHITE);
        setBackground(color);
    }

    @Override
    public void paint(Graphics g) {
        if (buttonImage == null || buttonImage.getWidth() != getWidth() ||
                buttonImage.getHeight() != getHeight()) {
            buttonImage = getGraphicsConfiguration().
                    createCompatibleImage(getWidth(), getHeight());
        }

        Graphics gButton = buttonImage.getGraphics();
        gButton.setClip(g.getClip());
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setClip(new RoundRectangle2D.Double(2, 2, getWidth() - 4, getHeight() - 4, 5, 5));
        g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
        g2.drawImage(buttonImage, 0, 0, null);
    }

    public void setAlpha(float alpha){
        this.alpha = alpha;
        repaint();
    }

    public float getAlpha(){
        return alpha;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (isEnabled()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            Animator animator = new Animator(300);
            animator.addTarget(new PropertySetter(this, "alpha", 0.35f));
            animator.setAcceleration(0.2f);
            animator.setDeceleration(0.4f);
            animator.start();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        opacity = .0f;
        Animator animator = new Animator(300);
        animator.addTarget(new PropertySetter(this, "alpha", 0f));
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.4f);
        animator.start();
    }
}
