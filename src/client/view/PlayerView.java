package client.view;

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

//TODO: Gestire la dimensione minima che può assumere la View! (Utile per risoluzioni molto basse)

public class PlayerView extends JPanel{
    private JLabel nickname;
    private JLabel action;
    private final static int END_BORDER_PADDING = 3;
    private final static float BORDER_WIDTH = 3.0F;
    private JLabel ranking;
    private JLabel avatar;
    private JLabel chips;
    private CardsPanel cardsPanel;

    private final static int START_BORDER_PADDING = 1;
    private JLabel position;
    private final static int VIEW_PADDING = 10;
    private Dimension size;
    private final static int MINIMUM_WIDTH = 350;
    private final static int MINIMUM_HEIGHT = 150;
    public final static String BLANK = "";

    /**
     * Costruttore della PlayerView. L'idea è ottenere tutte le informazioni da mostrare all'utente attraverso un Controller
     * specifico che rimane in attesa di ottenerle da Server. Risulta così più modulare il tutto.
     * Questa View sfrutta il BoxLayout, che permette di gestire i suoi componenti tramite le MaximumSize, in modo che si adattino
     * al cambiamento di dimensione della View padre.
     *
     * @param size Dimensione della View.
     */

    public PlayerView(Dimension size) {
        this.size = size;
        nickname = new JLabel();
        chips = new JLabel();
        position = new JLabel();
        action = new JLabel();
        ranking = new JLabel();
        cardsPanel = new CardsPanel(new Dimension((size.width) / 5, size.height / 2), 2);
        setBackground(Utils.TRANSPARENT);
        initView();
        initAvatarPanel("avatars/celebrity/dario zappa.png");
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
    }

    /**
     * Inizializzazione dell'avatar. Il BoxLayout favorisce l'utilizzo delle MaximumSize e delle PreferredSize, quindi
     * è consigliabile settare una dimensione massima per evitare bug grafici e sfruttare al meglio il layout.
     * Se il layout è costretto per qualsiasi motivo a essere esteso oltre la dimensione prevista il componente
     * avrà in tal caso un allineamento a sinistra.
     */

    private void initAvatarPanel(String avatarFilename){
        avatar = new JLabel(BLANK, JLabel.CENTER);
        BufferedImage avatarScaled = Utils.loadImage(avatarFilename, new Dimension((size.width / 3), size.height / 2));
        avatar.setIcon(new ImageIcon(avatarScaled));
        avatar.setMaximumSize(new Dimension((size.width / 3), size.height / 2));
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
        playerPanel.setMaximumSize(new Dimension((2 * size.width) / 3, size.height));
        playerPanel.setLayout(new GridBagLayout());

        playerPanel.add(cardsPanel, new GBC(0, 0, 1, 4, NORTHWEST));

        setComponentStyle(ranking, Color.WHITE, Font.BOLD, 26F);
        playerPanel.add(ranking, new GBC(1, 0,1, 4, NORTHEAST));

        setComponentStyle(nickname, Color.WHITE, Font.BOLD, 20F);
        playerPanel.add(nickname, new GBC(0, 1,1, 5, NORTHWEST));

        setComponentStyle(position, Color.WHITE, Font.BOLD, 20F);
        playerPanel.add(position, new GBC(1, 1, 1, 5, NORTHEAST));

        setComponentStyle(action, Color.WHITE, Font.BOLD, 18F);
        playerPanel.add(action, new GBC(0, 2, 1, 1, WEST));

        setComponentStyle(chips, Color.WHITE, Font.BOLD, 18F);
        chips.setFont(new Font("helvetica", Font.BOLD, 20));
        playerPanel.add(chips, new GBC(1, 2, 1, 1, EAST));
        add(playerPanel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;


        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);

        g2.setColor(new Color(27, 27, 27, 150));
        g2.fillRoundRect(START_BORDER_PADDING, START_BORDER_PADDING, size.width - END_BORDER_PADDING, size.height - END_BORDER_PADDING,
                40, 40);

        g2.setStroke(new BasicStroke(BORDER_WIDTH));
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(1, 1, size.width - END_BORDER_PADDING, size.height - END_BORDER_PADDING,
                40, 40);
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
        return size;
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
        return size;
    }

    public JLabel getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname.setText(nickname);
    }

    public void setChips(int chips) {
        this.chips.setText(Integer.toString(chips));
        this.chips.setBackground(new Color(0, 77, 64, 255));
        this.chips.setOpaque(true);

    }

    public void setAvatar(String filename) {
        BufferedImage avatarScaled = Utils.loadImage(filename, new Dimension((size.width / 3), size.height / 2));
        avatar.setIcon(new ImageIcon(avatarScaled));
    }

    public void setPosition(String actualPosition) {
        this.position.setText(actualPosition);
    }
}
