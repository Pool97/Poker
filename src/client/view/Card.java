package client.view;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

public class Card extends JComponent implements ComponentListener {
    private final static int TOP_LEFT = 0;
    private BufferedImage frontImage;
    private BufferedImage backImage;
    private String frontImageDirectoryPath;
    private String backImageDirectoryPath;
    private boolean isCovered;

    public Card(String frontImageDirectoryPath, String backImageDirectoryPath) {
        this.frontImageDirectoryPath = frontImageDirectoryPath;
        this.backImageDirectoryPath = backImageDirectoryPath;
        setLayout(new BorderLayout());
        isCovered = false;
        addComponentListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if ((frontImage == null && !isCovered) || (backImage == null && isCovered))
            return;

        if (isCovered)
            g.drawImage(backImage, TOP_LEFT, TOP_LEFT, null);
        else
            g.drawImage(frontImage, TOP_LEFT, TOP_LEFT, null);
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
            backImage = Utils.loadImageByPath(backImageDirectoryPath, getSize());
        else
            frontImage = Utils.loadImageByPath(frontImageDirectoryPath, getSize());
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
