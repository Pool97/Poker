package client.ui.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static utils.Utils.EMPTY;

public class Avatar extends JComponent implements MouseListener, ComponentListener {
    private Image image;
    private String directoryPath;
    private boolean isOpacity;
    private boolean isMinimum;
    private int dimension;
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
        directoryPath = "res/" + AVATARS_DIRECTORY + "/" + avatarName;
    }

    public void setMinimumSize(boolean isMinimum){
        this.isMinimum = isMinimum;
    }

    public void loadImage() {
        System.out.println(getWidth() + " " + getHeight());
        dimension = Math.min(getWidth(), getHeight());
        image = Utils.loadImageByPath(directoryPath, new Dimension(dimension-2 , dimension-3));
    }

    public boolean isImageLoaded(Image image) {
        return image != null;
    }

    public Dimension getMinimumSize() {
        return new Dimension(80, 80);
    }

    @Override
    public Dimension getMaximumSize() {
        if(isMinimum){
            return new Dimension(70, 70);
        }
        return new Dimension(100, 100);
    }

    @Override
    public Dimension getPreferredSize() {
        if(isMinimum){
            return getMinimumSize();
        }
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
            g.drawImage(image, 0, 1, null);
            g.drawOval(1, 1, dimension-3, dimension-3);
        }


        if (isOpacity)
            applyOpacity((Graphics2D) g.create(), opacity);

    }

    public void applyOpacity(Graphics2D g2D, float alpha) {
        g2D.setRenderingHints(Utils.getHighQualityRenderingHints());
        AlphaComposite composite = AlphaComposite.SrcOver;
        g2D.setComposite(composite.derive(alpha).derive(AlphaComposite.SRC_OVER));
        g2D.setColor(Color.DARK_GRAY);
        g2D.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2D.dispose();
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
