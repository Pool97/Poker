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


//TODO: Implementare un ComponentListener in modo che si abbia il resize automatico in base al cambio delle dimensioni della View

public class PlayerView extends JPanel{
    private JLabel nickname;
    private JLabel action;
    private JLabel totalChips;
    private JLabel actualPosition;
    private JLabel ranking;
    private Dimension viewSize;

    private final static int START_BORDER_PADDING = 1;
    private final static int END_BORDER_PADDING = 2;
    private final static int VIEW_PADDING = 10;
    private final static float BORDER_WIDTH = 2.0F;
    private final static int MINIMUM_WIDTH = 350;
    private final static int MINIMUM_HEIGHT = 150;
    public final static String AVATAR_FILE_NAME = "avatar.png";
    public final static String BLANK = "";

    /**
     * Costruttore della PlayerView. L'idea è ottenere tutte le informazioni da mostrare all'utente attraverso un Controller
     * specifico che rimane in attesa di ottenerle da Server. Risulta così più modulare il tutto.
     * Questa View sfrutta il BoxLayout, che permette di gestire i suoi componenti tramite le MaximumSize, in modo che si adattino
     * al cambiamento di dimensione della View padre.
     *
     * @param viewSize Dimensione della View.
     * @param playerModel Informazioni provenienti dal server. Rimarrà come argomento del costruttore finchè non verrà implementato a tutti
     *                    li effetti il Pattern MVC.
     */

    //TODO: Implementare il pattern MVC Server-Client in modo da aggiornare la View solo tramite le modifiche avvenute nell'effettivo Model.

    //A scopo di test, le informazioni vengono ricevute come argomento del costruttore ma in futuro verranno integrate tramite opportuni listeners.

    public PlayerView(Dimension viewSize, PlayerModel playerModel) {
        this.viewSize = viewSize;
        nickname = new JLabel(playerModel.getNickname());
        totalChips = new JLabel(playerModel.getTotalChips());
        actualPosition = new JLabel(playerModel.getActualPosition());
        action = new JLabel(playerModel.getAction());
        ranking = new JLabel(playerModel.getRanking());

        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setBorder(BorderFactory.createEmptyBorder(VIEW_PADDING, VIEW_PADDING, VIEW_PADDING, VIEW_PADDING));
        setLayout(layout);
        setBackground(new Color(27,27,27, 150));
        initAvatarPanel();
        add(Box.createRigidArea(new Dimension(VIEW_PADDING, 0)));
        initPlayerPanel(playerModel.getFirstCardFilename(), playerModel.getSecondCardFilename());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(BORDER_WIDTH));
        g2.drawRect(START_BORDER_PADDING, START_BORDER_PADDING, viewSize.width - END_BORDER_PADDING, viewSize.height - END_BORDER_PADDING);
    }

    /**
     * Inizializzazione dell'avatar. Il BoxLayout favorisce l'utilizzo delle MaximumSize e delle PreferredSize, quindi
     * è consigliabile settare una dimensione massima per evitare bug grafici e sfruttare al meglio il layout.
     * Se il layout è costretto per qualsiasi motivo a essere esteso oltre la dimensione prevista il componente
     * avrà in tal caso un allineamento a sinistra.
     */

    private void initAvatarPanel(){
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
     * @param firstFilename Nome del file della prima carta.
     * @param secondFilename Nome del file della seconda carta.
     */

    private void initPlayerPanel(String firstFilename, String secondFilename){
        JPanel container = new JPanel();
        container.setAlignmentX(Component.RIGHT_ALIGNMENT);
        container.setBackground(new Color(0,0,0,0));

        GridBagLayout playerInfoLayout = new GridBagLayout();
        container.setMaximumSize(new Dimension((2 * viewSize.width)/3, viewSize.height));
        container.setLayout(playerInfoLayout);

        GBC cardsConstr = new GBC(0, 0, 1, 4, NORTHWEST);
        container.add(initCardsPanel(firstFilename, secondFilename), cardsConstr);

        GBC rankConstr = new GBC(1, 0,1, 4, NORTHEAST);
        setComponentStyle(ranking, Color.WHITE, Font.BOLD, 26F);
        container.add(ranking, rankConstr);

        GBC nickConstr = new GBC(0, 1,1, 5, NORTHWEST);
        setComponentStyle(nickname, Color.WHITE, Font.BOLD, 20F);
        container.add(nickname, nickConstr);

        GBC positionConstr = new GBC(1, 1, 1, 5, NORTHEAST);
        setComponentStyle(actualPosition, Color.WHITE, Font.BOLD, 20F);
        container.add(actualPosition, positionConstr);

        GBC actionConstr = new GBC(0, 2, 1, 1, WEST);
        setComponentStyle(action, Color.WHITE, Font.BOLD, 18F);
        container.add(action, actionConstr);

        setComponentStyle(totalChips, Color.WHITE, Font.BOLD, 18F);
        GBC chipsConstr = new GBC(1, 2,1, 1, EAST);

        container.add(totalChips, chipsConstr);
        add(container);
    }

    /**
     * Inizializzazione del pannello contenente le carte del player avversario.
     * @param firstFilename Nome del file della prima carta.
     * @param secondFilename Nome del file della seconda carta.
     * @return Pannello.
     */
    //TODO: Da aggiungere come attributi le CardView in modo da poterle modificare dall'esterno. Questo andrà fatto appena funzionerà MVC.

    private JPanel initCardsPanel(String firstFilename, String secondFilename){
        JPanel cardContainer = new JPanel();
        BoxLayout cardLayout = new BoxLayout(cardContainer, BoxLayout.X_AXIS);
        cardContainer.setLayout(cardLayout);

        cardContainer.setBackground(new Color(0,0,0, 0)); //per fare in modo che in fase di rendering si veda solo il colore di sfondo del layout sottostante

        CardView firstCard = new CardView(new Dimension((viewSize.width)/5, viewSize.height/2), firstFilename);
        firstCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardContainer.add(firstCard);

        CardView secondCard = new CardView(new Dimension((viewSize.width)/5, viewSize.height/2), secondFilename);
        secondCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardContainer.add(secondCard);

        return cardContainer;
    }

    /**
     * Settaggio delle prorpietà dei componenti della View.
     * @param component Componente
     * @param foregroundColor Colore in primo piano
     * @param fontStyle Stile del font
     * @param fontSize Dimensione in pixel del font
     */

    private void setComponentStyle(JComponent component, Color foregroundColor, int fontStyle, float fontSize){
        component.setForeground(foregroundColor);
        component.setFont(Utils.getCustomFont(fontStyle, fontSize));
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

    /**
     * Impostazione della dimensione minima della View per il BoxLayout.
     * @return Dimensione minima.
     */

    //TODO: Gestire la dimensione minima che può assumere la View! (Utile per risoluzioni molto basse)

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT);
    }
}
