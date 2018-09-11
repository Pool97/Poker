package client.ui.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

public class Card extends JComponent implements ComponentListener {
    private final static int TOP_LEFT = 0;
    private final static String DEFAULT_IMAGE = System.getProperty(Utils.WORKING_DIRECTORY) + Utils.RES_DIRECTORY + "back.png";
    private final static String DEFAULT_BACK = System.getProperty(Utils.WORKING_DIRECTORY) + Utils.RES_DIRECTORY + "backOrangePP.png";
    private String frontImageDirectoryPath;
    private String backImageDirectoryPath;
    private boolean isCovered;
    private BufferedImage image;

    private Card(boolean isVisible, boolean isCovered) {
        this.frontImageDirectoryPath = DEFAULT_IMAGE;
        this.backImageDirectoryPath = DEFAULT_BACK;
        setVisible(isVisible);
        setCovered(isCovered);
        addComponentListener(this);
    }

    public static Card createEmptyCard() {
        return new Card(false, false);
    }

    public static Card createCard(boolean isCovered) {
        return new Card(true, isCovered);
    }

    public boolean isImageLoaded(BufferedImage image) {
        return image != null;
    }

    public void loadImage() {
        image = Utils.loadImageByPath(getDirectoryPathImageToLoad(), getSize());
    }

    public String getDirectoryPathImageToLoad() {
        return isCovered ? backImageDirectoryPath : frontImageDirectoryPath;
    }

    public void setCovered(boolean isCovered) {
        this.isCovered = isCovered;
    }

    public void setFrontImageDirectoryPath(String imageDirectoryPath) {
        this.frontImageDirectoryPath = System.getProperty("user.dir") + imageDirectoryPath;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHints(Utils.getHighQualityRenderingHints());
        if (isImageLoaded(image)) {
            g2D.drawImage(image, TOP_LEFT, TOP_LEFT, null);
        }
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
}
