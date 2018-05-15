package client.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

public class Card extends JComponent implements ComponentListener {
    private final static int TOP_LEFT = 0;
    private String frontImageDirectoryPath;
    private String backImageDirectoryPath;
    private boolean isCovered;
    private BufferedImage image;

    public Card(String frontImageDirectoryPath, String backImageDirectoryPath) {
        this.frontImageDirectoryPath = frontImageDirectoryPath;
        this.backImageDirectoryPath = backImageDirectoryPath;
        addComponentListener(this);
    }

    public boolean isImageLoaded(BufferedImage image) {
        return image != null;
    }

    public void setCovered(boolean isCovered) {
        this.isCovered = isCovered;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isImageLoaded(image))
            g.drawImage(image, TOP_LEFT, TOP_LEFT, null);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(80, 80);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMaximumSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(100, 100);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (isCovered)
            image = Utils.loadImageByPath(backImageDirectoryPath, getSize());
        else
            image = Utils.loadImageByPath(frontImageDirectoryPath, getSize());
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
}
