package client.ui.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Card extends JComponent implements ComponentListener {
    private final static String PLACEHOLDER_IMAGE = System.getProperty(Utils.WORKING_DIRECTORY) + Utils.RES_DIRECTORY + "back.png";
    private final static String DEFAULT_BACK = System.getProperty(Utils.WORKING_DIRECTORY) + Utils.RES_DIRECTORY + "backOrangePP.png";
    private String frontImageDirectoryPath;
    private String backImageDirectoryPath;
    private boolean isCovered;
    private Image image;

    private Card(boolean isVisible, boolean isCovered) {
        this.frontImageDirectoryPath = PLACEHOLDER_IMAGE;
        this.backImageDirectoryPath = DEFAULT_BACK;

        setOpaque(false);
        setVisible(isVisible);
        setCovered(isCovered);

        addComponentListener(this);
    }

    public static Card createPlaceholder() {
        return new Card(true, false);
    }

    public static Card createCard(boolean isCovered) {
        return new Card(true, isCovered);
    }

    private boolean isImageLoaded(Image image) {
        return image != null;
    }

    public void loadImage() {
        if(getWidth() != 0 && getWidth() != 0 && !frontImageDirectoryPath.equals(PLACEHOLDER_IMAGE))
            image = Utils.loadImageByPath(getDirectoryPathImageToLoad(), getSize());
    }

    private String getDirectoryPathImageToLoad() {
        return isCovered ? backImageDirectoryPath : frontImageDirectoryPath;
    }

    public void setCovered(boolean isCovered) {
        this.isCovered = isCovered;
        refresh();
    }

    public void setFrontImageDirectoryPath(String imageDirectoryPath) {
        this.frontImageDirectoryPath = System.getProperty("user.dir") + imageDirectoryPath;
        refresh();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHints(Utils.getHighQualityRenderingHints());

        if (isImageLoaded(image))
            g2D.drawImage(image, 2, 2, getWidth() - 4, getHeight() - 4, null);
        else{

            Composite composite = g2D.getComposite();
            g2D.setColor(new Color(100, 128, 150));
            g2D.setStroke(new BasicStroke(3));
            g2D.drawRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 10, 10);
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
            g2D.fillRoundRect(6, 6, getWidth() - 9, getHeight() - 9, 10, 10);
            g2D.setComposite(composite);

        }
    }

    private void refresh(){
        loadImage();
        repaint();
    }

    public boolean isPlaceHolder(){
        return frontImageDirectoryPath.equals(PLACEHOLDER_IMAGE);
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
        refresh();
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
