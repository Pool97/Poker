package graphics;

import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.GridBagConstraints.*;

/**
 * View che permette di creare lo scheletro dei Players avversari.
 */

//TODO: Configurare e gestire la dimensione minima che può assumere la View! (Utile per risoluzioni molto basse)
//TODO: Implementare un ComponentListener in modo che si abbia il resize automatico in base al cambio delle dimensioni della View

public class PlayerView extends JPanel {
    private JLabel nickname;
    private JLabel action;
    private JLabel totalChips;
    private JLabel actualPosition;
    //private ArrayList<CardView> cards;
    private JLabel ranking;
    private Dimension viewSize;

    private final static int START_BORDER_PADDING = 2;
    private final static int END_BORDER_PADDING = 4;
    private final static int VIEW_PADDING = 5;
    public final static String AVATAR_FILE_NAME = "avatar.png";
    public final static String BLANK = "";

    /**
     * Costruttore della PlayerView. L'idea è ottenere tutte le informazioni da mostrare all'utente attraverso un Controller
     * specifico che rimane in attesa di ottenerle da Server. Risulta così più modulare il tutto.
     * Questa View sfrutta il BoxLayout, che permette di gestire i suoi componenti tramite le MaximumSize, in modo che si adattino
     * al cambiamento di dimensione della View padre.
     *
     * @param viewSize Dimensione della View.
     * @param nickname Nickname del Player
     * @param totalChips Numero totale di chips a disposizione
     * @param actualPosition Posizione attuale (es: Dealer, Small Blind ecc..)
     * @param action Azione effettuata dal Player nel turno attuale
     * @param ranking Posizione attuale in classifica in termini di numero di chips a disposizione
     */

    //TODO: Restyling grafico dello scheletro base della View.

    public PlayerView(Dimension viewSize, String nickname, String totalChips, String actualPosition, String action, String ranking) {
        this.viewSize = viewSize;
        this.nickname = new JLabel(nickname);
        this.totalChips = new JLabel(totalChips);
        this.actualPosition = new JLabel(actualPosition);
        this.action = new JLabel(action);
        this.ranking = new JLabel(ranking);

        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setBorder(BorderFactory.createEmptyBorder(VIEW_PADDING, VIEW_PADDING, VIEW_PADDING, VIEW_PADDING));
        setLayout(layout);
        initAvatar();
        add(Box.createRigidArea(new Dimension(VIEW_PADDING, 0)));
        initPlayerPanel();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2.0F));
        g2.drawRect(START_BORDER_PADDING, START_BORDER_PADDING, viewSize.width - END_BORDER_PADDING, viewSize.height - END_BORDER_PADDING);
    }

    /**
     * Inizializzazione dell'avatar. Il BoxLayout favorisce l'utilizzo delle MaximumSize e delle PreferredSize, quindi
     * è consigliabile settare una dimensione massima per evitare bug grafici e sfruttare al meglio il layout.
     * Se il layout è costretto per qualsiasi motivo a essere esteso oltre la dimensione prevista il componente
     * avrà in tal caso un allineamento a sinistra.
     */

    //TODO Implementare una classe separata per la gestione dell'Avatar!
    private void initAvatar(){
        JLabel avatar = new JLabel(BLANK, JLabel.CENTER);
        BufferedImage avatarScaled = Utils.loadImage(AVATAR_FILE_NAME, new Dimension((viewSize.width / 3), viewSize.height / 2));
        avatar.setIcon(new ImageIcon(avatarScaled));
        avatar.setMaximumSize(new Dimension((viewSize.width / 3), viewSize.height / 2));
        avatar.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(avatar);
    }

    /**
     * Inizializzazione del pannello delle informazioni relative al Player avversario.
     * Se il layout è costretto per qualsiasi motivo a essere esteso oltre la dimensione
     * prevista il componente avrà in tal caso un allineamento a destra.
     */

    private void initPlayerPanel(){
        JPanel container = new JPanel();
        GridBagLayout playerInfoLayout = new GridBagLayout();
        container.setMaximumSize(new Dimension((2 * viewSize.width)/3, viewSize.height));
        container.setLayout(playerInfoLayout);

        GBC cardsConstr = new GBC(0, 0, 1, 1, WEST);
        container.add(new JButton("Placeholder"), cardsConstr); //TODO: da aggiungere ancora le carte del Player!
        GBC rankConstr = new GBC(1, 0,100, 100, NORTHEAST);
        container.add(ranking, rankConstr);
        GBC nickConstr = new GBC(0, 1,10, 10, WEST);
        container.add(nickname, nickConstr);
        GBC positionConstr = new GBC(1, 1, 10, 10, EAST);
        container.add(actualPosition, positionConstr);
        GBC actionConstr = new GBC(0, 2, 10, 10, WEST);
        container.add(action, actionConstr);
        GBC chipsConstr = new GBC(1, 2,10, 10, EAST);
        container.add(totalChips, chipsConstr);
        container.setAlignmentX(Component.RIGHT_ALIGNMENT);
        add(container);

    }

    /**
     * Impostazione della dimensione preferita della View per il BoxLayout. In fase di rendering grafico viene settata
     * automaticamente questa dimensione. Se qualche componente al suo interno ha una dimensione più grande dello stesso,
     * il layout si riadatta alla dimensione massima tra tutti i componenti presenti al suo interno.
     *
     * @return Dimensione preferita.
     */

    @Override
    public Dimension getPreferredSize() {
        return viewSize;
    }
}
