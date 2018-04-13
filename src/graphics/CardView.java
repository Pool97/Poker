package graphics;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * View che permette di modellare il comportamento di una carta da Poker.
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class CardView extends JPanel{
    private BufferedImage cardBack;
    private BufferedImage cardFront;
    private Dimension viewSize;
    private boolean isCovered;

    private final static int TOP_LEFT_CORNER = 0;

    /**
     * Costruttore della view CardView. L'idea è ottenere il nome del file della carta da caricare nell'oggetto attraverso
     * un Controller specifico, incaricato di restare in ascolto del Server.
     *
     * @param viewSize Dimensione della CardView
     * @param cardFrontFilename Nome del file della parte frontale della Card.
     * @param cardBackFilename Nome del file della parte posteriore della Card.
     */

    public CardView(Dimension viewSize, String cardFrontFilename, String cardBackFilename){
        this.viewSize = viewSize;
        this.cardFront = Utils.loadImage(cardFrontFilename, viewSize);
        this.cardBack = Utils.loadImage(cardBackFilename, viewSize);

        isCovered = false;
        setSize(viewSize);
        //setOpaque(false);
        setBackground(Utils.TRANSPARENT);
    }

    /**
     * Permette di impostare la carta in modalità coperta o scoperta. Rispettivamente verranno mostrate a schermo la parte
     * frontale e la parte posteriore della carta.
     * Di default la carta opera in modalità scoperta.
     *
     * @param isCovered Modalità coperta se true, modalità scoperta se false.
     */

    public void setCovered(boolean isCovered){
        this.isCovered = isCovered;
    }

    /**
     * Restituisce la modalità in cui è mostrata a schermo la carta.
     * @return Modalità coperta se true, modalità scoperta se false.
     */

    public boolean isCovered(){
        return isCovered;
    }

    /**
     * Permette il rendering a schermo della CardView.
     * @see JComponent#paintComponent(Graphics);
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if((cardFront == null && !isCovered) || (cardBack == null && isCovered))
            return;

        if(isCovered)
            g.drawImage(cardBack, TOP_LEFT_CORNER, TOP_LEFT_CORNER, null);
        else
            g.drawImage(cardFront, TOP_LEFT_CORNER, TOP_LEFT_CORNER, null);
    }

    /**
     * Imposta la dimensione preferita con la quale si vuol mostrate la carta a schermo.
     * L'effettiva dimensione del rendering dipenderà però dalle dimensioni e dalla tipologia
     * di LayoutManager presente nel container della CardView.
     *
     * @return Dimensione preferita per il rendering.
     */

    public Dimension getPreferredSize(){
        return viewSize;
    }
}
