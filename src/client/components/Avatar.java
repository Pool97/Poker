package client.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import static utils.Utils.*;

public class Avatar extends JComponent implements MouseListener, ComponentListener {
    private BufferedImage image;
    private String directoryPath;
    private boolean isOpacity;

    private static final String AVATARS_DIRECTORY = "avatars";
    private float opacity;
    private static final String IMAGE_EXTENSION = ".png";

    public Avatar(String avatarName) {
        setDirectoryPath(avatarName);
        addComponentListener(this);
        addMouseListener(this);
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setOpacity(boolean isOpacity) {
        this.isOpacity = isOpacity;
    }

    public String getName() {
        String filenameWithExtension = directoryPath.substring(directoryPath.lastIndexOf("/") + 1);
        return filenameWithExtension.replace(IMAGE_EXTENSION, EMPTY);
    }

    public void setDirectoryPath(String avatarName) {
        directoryPath = System.getProperty(WORKING_DIRECTORY) + RES_DIRECTORY + AVATARS_DIRECTORY + "/" + avatarName;
    }

    public void loadImage() {
        image = Utils.loadImageByPath(directoryPath, new Dimension(getWidth() - 6, getHeight() - 6));
    }

    public boolean isImageLoaded(BufferedImage image) {
        return image != null;
    }

    public Dimension getMinimumSize() {
        return new Dimension(80, 80);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(100, 100);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMaximumSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHints(Utils.getHighQualityRenderingHints());
        g2D.setColor(Color.WHITE);
        g2D.setStroke(new BasicStroke(2));
        if (isImageLoaded(image)) {
            int minimum = Math.min(getWidth(), getHeight());
            g.drawImage(image, 3, 3, null);
            g.drawOval(2, 2, minimum - 4, minimum - 3);
        }

        if (isOpacity)
            applyOpacity((Graphics2D) g, opacity);

    }

    public void applyOpacity(Graphics2D g2D, float alpha) {
        g2D.setRenderingHints(Utils.getHighQualityRenderingHints());
        Composite oldComposite = g2D.getComposite();
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2D.setColor(Color.DARK_GRAY);
        g2D.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2D.setComposite(oldComposite);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        loadImage();
        repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

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
        if (isOpacity) {
            opacity = .25f;
            repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (isOpacity) {
            opacity = .0f;
        }
    }
}
