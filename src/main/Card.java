package main;

import graphics.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card extends JPanel implements MouseMotionListener, MouseListener {
    private Point2D mousePos;
    private int screenX;
    private int screenY;
    private int myX = 0;
    private int myY = 0;
    private BufferedImage card;
    private String faceName;
    private String suit;
    private int faceValue;

    public Card(){
        addMouseListener(this);
        addMouseMotionListener(this);
        setOpaque(false);
        try {
            BufferedImage originalImage = ImageIO.read(new File(System.getProperty("user.dir").concat("/res/2_of_clubs.png")));
            card = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.BEST_FIT_BOTH, 111, 162, Scalr.OP_DARKER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //card = new ImageIcon(System.getProperty("user.dir").concat("/res/2_of_clubs.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(card == null)
            return;
        Graphics2D g2 = (Graphics2D)g;

        g2.setStroke(new BasicStroke(2.0F));
        g2.drawRect(2,2, 127, 177);
        int offsetX = (getWidth() - 111)/2;
        int offsety = (getHeight() - 162)/2;
        g2.drawImage(card, offsetX,offsety, 111, 162, null);
    }

    public Dimension getPreferredSize(){
        return new Dimension(130,180);
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        mousePos = e.getPoint();
        int deltaX = e.getXOnScreen() - screenX;
        int deltaY = e.getYOnScreen() - screenY;

        setLocation(myX + deltaX, myY + deltaY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point mousePosition = e.getPoint();

        if((e.getLocationOnScreen().getX() >= getX() && e.getLocationOnScreen().getX() <= getX() + getWidth()) && (mousePosition.getY() >= getY() && mousePosition.getY() <= getY() + getHeight()))
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();

        myX = getX();
        myY = getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
