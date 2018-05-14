package client.view;

import client.AvatarCategory;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import static utils.Utils.RES;
import static utils.Utils.WORKING_DIR;

public class Avatar extends JComponent implements ComponentListener {
    private static final String AVATARS_FOLDER = "/avatars/";
    private static final String IMAGE_EXTENSION = ".png";
    private BufferedImage image;
    private String directoryPath;

    public Avatar(String directoryPath) {
        this.directoryPath = directoryPath;
        addComponentListener(this);
    }

    public Avatar(AvatarCategory category, String avatarName) {
        setDirectoryPath(category, avatarName);
        addComponentListener(this);
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getName() {
        String nameWithExtension = directoryPath.substring(directoryPath.lastIndexOf("/") + 1, directoryPath.length());
        return nameWithExtension.replace(IMAGE_EXTENSION, Utils.EMPTY);
    }

    public void setDirectoryPath(AvatarCategory category, String avatarName) {
        directoryPath = System.getProperty(WORKING_DIR) + RES + AVATARS_FOLDER + category + "/" + avatarName;
    }

    @Override
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
        if (image != null)
            g.drawImage(image, 0, 0, null);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        image = Utils.loadImageByPath(directoryPath, getSize());
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
