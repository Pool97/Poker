package client.ui.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Card extends JComponent implements ComponentListener {
    private final static int TOP_LEFT = 0;
    private final static String DEFAULT_IMAGE = System.getProperty(Utils.WORKING_DIRECTORY) + Utils.RES_DIRECTORY + "back.png";
    private final static String DEFAULT_BACK = System.getProperty(Utils.WORKING_DIRECTORY) + Utils.RES_DIRECTORY + "backOrangePP.png";
    private String frontImageDirectoryPath;
    private String backImageDirectoryPath;
    private boolean isCovered;
    private Image image;

    private Card(boolean isVisible, boolean isCovered) {
        this.frontImageDirectoryPath = DEFAULT_IMAGE;
        this.backImageDirectoryPath = DEFAULT_BACK;
        setOpaque(false);
        setVisible(isVisible);
        setCovered(isCovered);
        addComponentListener(this);
    }

    private Card(boolean isVisible, boolean isCovered, String cardBackName) {
        this.frontImageDirectoryPath = DEFAULT_IMAGE;
        this.backImageDirectoryPath = System.getProperty(Utils.WORKING_DIRECTORY) + Utils.RES_DIRECTORY + cardBackName;
        setVisible(isVisible);
        setCovered(isCovered);
        addComponentListener(this);
        setOpaque(false);
    }

    public static Card createEmptyCard() {
        return new Card(true, false);
    }

    public static Card createCard(boolean isCovered) {
        return new Card(true, isCovered);
    }

    public static Card createCard(boolean isCovered, String cardBackName){
        return new Card(true, isCovered, cardBackName);
    }

    public boolean isImageLoaded(Image image) {
        return image != null;
    }

    public void loadImage() {
        if(getSize().width != 0 && getSize().height != 0 && !frontImageDirectoryPath.equals(DEFAULT_IMAGE))
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
            g2D.drawImage(image, 2, 2, getWidth() - 4, getHeight() - 4, null);
        }else{
            Composite composite = g2D.getComposite();
            g2D.setColor(new Color(100, 128, 150));
            g2D.setStroke(new BasicStroke(3));
            g2D.drawRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 10, 10);
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
            g2D.fillRoundRect(6, 6, getWidth() - 9, getHeight() - 9, 10, 10);
            g2D.setComposite(composite);

        }
    }

    public boolean isDefault(){
        return frontImageDirectoryPath.equals(DEFAULT_IMAGE);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(60, 70);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMaximumSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(80, 100);
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
