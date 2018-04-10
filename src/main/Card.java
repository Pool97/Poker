package main;

import graphics.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * View che crea lo scheletro della carta da Poker.
 */

public class Card extends JPanel{
    private static BufferedImage cardBack;
    private BufferedImage cardFront;
    private Dimension imageSize;

    public static final String RES = "/res/";
    public static final String WORKING_DIR = "user.dir";
    public static final String CARD_BACK_FILE_NAME = "back.png";
    public static final int IMAGE_PADDING = 20;
    public static final int LEFT_PADDING = 2;
    public static final int RIGHT_PADDING = 3;


    /**
     * Costruttore della view Card. L'idea è ottenere il nome del file della carta da caricare nell'oggetto attraverso
     * un Controller specifico, incaricato di restare in ascolto del Server.
     * @param cardSize Dimensione della View.
     * @param filename Nome del file associato alla carta da caricare.
     */

    public Card(Dimension cardSize, String filename){
        this.imageSize = new Dimension(cardSize.width - IMAGE_PADDING, cardSize.height - IMAGE_PADDING);
        this.cardFront = loadImage(filename, imageSize.getSize());
        setSize(cardSize);
        cardBack = loadImage(CARD_BACK_FILE_NAME, imageSize.getSize());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(cardFront == null)
            return;

        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2.0F));
        g2.drawRect(LEFT_PADDING,LEFT_PADDING, getWidth() - RIGHT_PADDING, getHeight() - RIGHT_PADDING);
        int offsetX = (getWidth() - imageSize.width)/2;
        int offsetY = (getHeight() - imageSize.height)/2;
        g2.drawImage(cardFront, offsetX, offsetY, null);

    }

    /**
     * Dimensione ottimizzata per i FlowLayout.
     * Per i BorderLayout è consigliato inserire la Card all'interno di un container (es:JPanel) per evitare
     * che le dimensioni si modifichino a causa della logica del BorderLayout.
     * @return Dimensione ottimale per i FlowLayout.
     */

    public Dimension getPreferredSize(){
        return new Dimension(getWidth(), getHeight());
    }

    /**
     * Permette di assegnare la versione riscalata dell'immagine originale alla Card.
     * @param filename Nome del file relativo all'immagine da caricare.
     * @param scaleSize Dimensione dell'immagine riscalata.
     * @return Immagine riscalata.
     */

    private static BufferedImage loadImage(String filename, Dimension scaleSize){
        BufferedImage scaledImage = null;

        try {
            BufferedImage originalImage = ImageIO.read(new File(System.getProperty(WORKING_DIR) + RES + filename));
            scaledImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.BEST_FIT_BOTH, (int)scaleSize.getWidth(), (int)scaleSize.getHeight(), Scalr.OP_DARKER);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scaledImage;
    }


    /*private String getImageFileName(){
        StringBuilder fileName = new StringBuilder(System.getProperty("user.dir"));
        fileName.append(RES).append(faceName).append("_").append(suit).append(".png");
        return fileName.toString();
    }*/
}
