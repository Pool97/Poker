package client.ui.userboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import static java.awt.Font.PLAIN;
import static utils.Utils.DEFAULT_FONT;

public class ActionButton extends JButton implements MouseListener {
    public float opacity;
    BufferedImage buttonImage = null;
    Color color;

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
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.WHITE);
        AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        g2D.setComposite(newComposite);

        g2D.drawImage(buttonImage, 0, 0, null);
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
            opacity = .20f;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        opacity = .0f;
    }
}
