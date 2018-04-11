package graphics;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * View che crea lo scheletro della carta da Poker.
 */

public class CardView extends JPanel{
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
     * Costruttore della view CardView. L'idea è ottenere il nome del file della carta da caricare nell'oggetto attraverso
     * un Controller specifico, incaricato di restare in ascolto del Server.
     * @param cardSize Dimensione della View.
     * @param filename Nome del file associato alla carta da caricare.
     */

    public CardView(Dimension cardSize, String filename){
        this.imageSize = new Dimension(cardSize.width - IMAGE_PADDING, cardSize.height - IMAGE_PADDING);
        this.cardFront = Utils.loadImage(filename, imageSize.getSize());
        setSize(cardSize);
        cardBack = Utils.loadImage(CARD_BACK_FILE_NAME, imageSize.getSize());
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
     * Per i BorderLayout è consigliato inserire la CardView all'interno di un container (es:JPanel) per evitare
     * che le dimensioni si modifichino a causa della logica del BorderLayout.
     * @return Dimensione ottimale per i FlowLayout.
     */

    public Dimension getPreferredSize(){
        return new Dimension(getWidth(), getHeight());
    }


}
