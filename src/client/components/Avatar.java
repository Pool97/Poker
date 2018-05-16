package client.components;

import client.AvatarCategory;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import static utils.Utils.*;

public class Avatar extends JComponent implements ComponentListener {
    private BufferedImage image;
    private String directoryPath;

    private static final String AVATARS_DIRECTORY = "/avatars/";
    private static final String IMAGE_EXTENSION = ".png";

    public Avatar(String directoryPath) {
        this.directoryPath = directoryPath;
        addComponentListener(this);
    }

    public Avatar(AvatarCategory category, String avatarName) {
        setDirectoryPath(category, avatarName);
        addComponentListener(this);
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public String getName() {
        String filenameWithExtension = directoryPath.substring(directoryPath.lastIndexOf("/") + 1, directoryPath.length());
        return filenameWithExtension.replace(IMAGE_EXTENSION, EMPTY);
    }

    public void setDirectoryPath(AvatarCategory category, String avatarName) {
        directoryPath = System.getProperty(WORKING_DIRECTORY) + RES_DIRECTORY + AVATARS_DIRECTORY + category + "/" + avatarName;
    }

    public void loadImage() {
        image = Utils.loadImageByPath(directoryPath, getSize());
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

        if (isImageLoaded(image))
            g.drawImage(image, 0, 0, null);
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
