package graphics;

import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.GridBagConstraints.*;

/**
 * View che permette di mostrare in real-time la situazione dei Player avversari in tutti gli istanti del match.
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */


//TODO: Implementare un ComponentListener in modo che si abbia il resize automatico in base al cambio delle dimensioni della View
//TODO: Implementare il pattern MVC Server-Client in modo da aggiornare la View solo tramite le modifiche avvenute nell'effettivo Model.
//TODO: Gestire la dimensione minima che può assumere la View! (Utile per risoluzioni molto basse)

public class PlayerView extends JPanel{
    private JLabel nickname;
    private JLabel action;
    private JLabel totalChips;
    private JLabel actualPosition;
    private JLabel ranking;
    private Dimension viewSize;
    private CardsPanel cardsPanel;

    private final static int START_BORDER_PADDING = 1;
    private final static int END_BORDER_PADDING = 2;
    private final static int VIEW_PADDING = 10;
    private final static float BORDER_WIDTH = 2.0F;
    private final static int MINIMUM_WIDTH = 350;
    private final static int MINIMUM_HEIGHT = 150;
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

    public PlayerView(Dimension viewSize, PlayerModel playerModel) {
        this.viewSize = viewSize;
        nickname = new JLabel(playerModel.getNickname());
        totalChips = new JLabel(playerModel.getTotalChips());
        actualPosition = new JLabel(playerModel.getActualPosition());
        action = new JLabel(playerModel.getAction());
        ranking = new JLabel(playerModel.getRanking());
        cardsPanel = new CardsPanel(2);
        cardsPanel.addNextCard(new CardView(new Dimension((viewSize.width)/5, viewSize.height/2), playerModel.getFirstCardFilename(), "backBluePP.png"));
        cardsPanel.addNextCard(new CardView(new Dimension((viewSize.width)/5, viewSize.height/2), playerModel.getSecondCardFilename(), "backBluePP.png"));

        initView();
        initAvatarPanel(playerModel.getAvatarFilename());
        add(Box.createRigidArea(new Dimension(VIEW_PADDING, 0)));
        initPlayerPanel();
    }

    /**
     * Inizializzazione della PlayerView. Seguiranno le inizializzazioni di tutte le sottoparti che compongono
     * la View.
     */

    private void initView(){
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setBorder(BorderFactory.createEmptyBorder(VIEW_PADDING, VIEW_PADDING, VIEW_PADDING, VIEW_PADDING));
        setLayout(layout);
        setBackground(new Color(27,27,27, 150));
    }

    /**
     * Inizializzazione dell'avatar. Il BoxLayout favorisce l'utilizzo delle MaximumSize e delle PreferredSize, quindi
     * è consigliabile settare una dimensione massima per evitare bug grafici e sfruttare al meglio il layout.
     * Se il layout è costretto per qualsiasi motivo a essere esteso oltre la dimensione prevista il componente
     * avrà in tal caso un allineamento a sinistra.
     */

    private void initAvatarPanel(String avatarFilename){
        JLabel avatar = new JLabel(BLANK, JLabel.CENTER);
        BufferedImage avatarScaled = Utils.loadImage(avatarFilename, new Dimension((viewSize.width / 3), viewSize.height / 2));
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
        JPanel playerPanel = new JPanel();
        playerPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        playerPanel.setBackground(Utils.TRANSPARENT);
        playerPanel.setMaximumSize(new Dimension((2 * viewSize.width)/3, viewSize.height));
        playerPanel.setLayout(new GridBagLayout());

        playerPanel.add(cardsPanel, new GBC(0, 0, 1, 4, NORTHWEST));

        setComponentStyle(ranking, Color.WHITE, Font.BOLD, 26F);
        playerPanel.add(ranking, new GBC(1, 0,1, 4, NORTHEAST));

        setComponentStyle(nickname, Color.WHITE, Font.BOLD, 20F);
        playerPanel.add(nickname, new GBC(0, 1,1, 5, NORTHWEST));

        setComponentStyle(actualPosition, Color.WHITE, Font.BOLD, 20F);
        playerPanel.add(actualPosition, new GBC(1, 1, 1, 5, NORTHEAST));

        setComponentStyle(action, Color.WHITE, Font.BOLD, 18F);
        playerPanel.add(action, new GBC(0, 2, 1, 1, WEST));

        setComponentStyle(totalChips, Color.WHITE, Font.BOLD, 18F);
        playerPanel.add(totalChips, new GBC(1, 2,1, 1, EAST));

        add(playerPanel);
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
     * Settaggio delle proprietà dei componenti della View.
     * @param component Componente
     * @param foregroundColor Colore in primo piano
     * @param fontStyle Stile del font
     * @param fontSize Dimensione in pixel del font
     */

    public void setComponentStyle(JComponent component, Color foregroundColor, int fontStyle, float fontSize){
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

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT);
    }

    @Override
    public Dimension getMaximumSize() {
        return viewSize;
    }
}
