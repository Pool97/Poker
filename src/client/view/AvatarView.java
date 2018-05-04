package client.view;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AvatarView extends JPanel {

    private final static int TOP_LEFT_CORNER = 2;
    private BufferedImage avatarFront;
    private Dimension viewSize;
    private String name;

    /**
     * Costruttore della view cards.CardView. L'idea è ottenere il nome del file della carta da caricare nell'oggetto attraverso
     * un Controller specifico, incaricato di restare in ascolto del Server.
     *
     * @param viewSize          Dimensione della cards.CardView
     * @param cardFrontFilename Nome del file della parte frontale della Card.
     */

    public AvatarView(Dimension viewSize, String cardFrontFilename) {
        this.viewSize = viewSize;
        name = cardFrontFilename;
        this.avatarFront = Utils.loadImage(cardFrontFilename, viewSize);
        setSize(viewSize);
        //setOpaque(false);
        setBackground(Utils.TRANSPARENT);
    }

    /**
     * Permette il rendering a schermo della cards.CardView.
     *
     * @see JComponent#paintComponent(Graphics);
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(avatarFront, TOP_LEFT_CORNER, TOP_LEFT_CORNER, null);
    }

    /**
     * Imposta la dimensione preferita con la quale si vuol mostrate la carta a schermo.
     * L'effettiva dimensione del rendering dipenderà però dalle dimensioni e dalla tipologia
     * di LayoutManager presente nel container della cards.CardView.
     *
     * @return Dimensione preferita per il rendering.
     */

    public Dimension getPreferredSize() {
        return viewSize;
    }

    public String getName() {
        return name;
    }
}
