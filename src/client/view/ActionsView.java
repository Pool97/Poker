package client.view;

import utils.GBC;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Hashtable;

/**
 * View che permette al Player di scegliere tra le azioni disponibili per una puntata di un turno.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class ActionsView extends JPanel {
    private final static String CHECK_FOLD_ACTION = "CHECK/FOLD";
    private final static String CALL_ACTION = "CALL";
    private final static String RAISE_ACTION = "RAISE";
    private final static int DEFAULT_MIN_VALUE_SLIDER = 0;
    private final static int DEFAULT_MAX_VALUE_SLIDER = 100;
    private final static int INSET = 30;
    private final static int WEIGHT = 33;
    private final static int INITIAL_PADDING = 2;
    private final static int FINAL_PADDING = 2;
    private final static int ARC_SIZE = 30;
    private final static float STROKE_WIDTH = 4.0F;
    private JButton callAction;
    private JButton checkAction;
    private JButton raiseAction;
    private JSlider raiseSlider;

    /**
     * Costruttore vuoto di ActionsView.
     */

    public ActionsView() {
        setBackground(Utils.TRANSPARENT);
        setLayout(new GridBagLayout());
        initActions();
        initSlider();
    }

    /**
     * Permette di inizializzare i pulsanti necessari per permettere al Player di effettuare la puntata a sua scelta.
     */

    private void initActions() {
        Font font = new Font(Utils.DEFAULT_FONT, Font.PLAIN, 30);

        checkAction = new JButton(CHECK_FOLD_ACTION);
        checkAction.setFont(font);
        add(checkAction, new GBC(0, 1, WEIGHT, 1, 1, 1, GBC.CENTER, GBC.HORIZONTAL,
                new Insets(0, INSET, 0, INSET)));

        callAction = new JButton(CALL_ACTION);
        callAction.setFont(font);
        add(callAction, new GBC(1, 1, WEIGHT, 1, 1, 1, GBC.CENTER, GBC.HORIZONTAL,
                new Insets(0, INSET, 0, INSET)));

        raiseAction = new JButton(RAISE_ACTION);
        raiseAction.setFont(font);
        add(raiseAction, new GBC(2, 1, WEIGHT, 1, 1, 1, GBC.CENTER, GBC.HORIZONTAL,
                new Insets(0, INSET, 0, INSET)));

    }

    /**
     * {@link JComponent#paintComponent(Graphics)}
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2D.setRenderingHints(qualityHints);

        g2D.setColor(new Color(171, 39, 60));
        g2D.fillRoundRect(INITIAL_PADDING, INITIAL_PADDING, getWidth() - FINAL_PADDING, getHeight() - FINAL_PADDING, ARC_SIZE, ARC_SIZE);
        g2D.setStroke(new BasicStroke(STROKE_WIDTH));
        g2D.setColor(Color.WHITE);
        g2D.drawRoundRect(INITIAL_PADDING, INITIAL_PADDING, getWidth() - FINAL_PADDING, getHeight() - FINAL_PADDING, ARC_SIZE, ARC_SIZE);
    }

    /**
     * Permette di inizializzare lo slider, necessario per permettere al Player di effettuare l'azione di Raise, con
     * un valore di Chips scelto tramite lo slider.
     */

    private void initSlider() {
        Font font = new Font(Utils.DEFAULT_FONT, Font.PLAIN, 20);
        raiseSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);

        SliderListener popupListener = new SliderListener();
        raiseSlider.addMouseMotionListener(popupListener);
        raiseSlider.addMouseListener(popupListener);
        raiseSlider.setMinorTickSpacing(DEFAULT_MIN_VALUE_SLIDER);
        raiseSlider.setMajorTickSpacing(DEFAULT_MAX_VALUE_SLIDER);
        raiseSlider.setPaintTicks(true);

        Hashtable<Integer, JLabel> position = new Hashtable<>();

        JLabel minValue = new JLabel(Integer.toString(DEFAULT_MIN_VALUE_SLIDER));
        minValue.setFont(font);
        minValue.setForeground(Color.WHITE);
        position.put(DEFAULT_MIN_VALUE_SLIDER, minValue);

        JLabel maxValue = new JLabel(Integer.toString(DEFAULT_MAX_VALUE_SLIDER));
        maxValue.setFont(font);
        maxValue.setForeground(Color.WHITE);
        position.put(DEFAULT_MAX_VALUE_SLIDER, maxValue);

        raiseSlider.setLabelTable(position);
        raiseSlider.setPaintLabels(true);
        add(raiseSlider, new GBC(2, 0, 1, 1, 1, 1, GBC.CENTER, GBC.HORIZONTAL));
    }

    /**
     * Permette di aggiungere un Listener al pulsante per l'azione di Call.
     *
     * @param listener
     */

    public void setCallActionListener(ActionListener listener) {
        callAction.addActionListener(listener);
    }

    /**
     * Permette di aggiungere un Listener al pulsante per l'azione di Check/Fold.
     *
     * @param listener
     */

    public void setCheckActionListener(ActionListener listener) {
        checkAction.addActionListener(listener);
    }

    /**
     * Permette di aggiungere un Listener al pulsante per l'azione di Raise.
     *
     * @param listener
     */

    public void setRaiseActionListener(ActionListener listener) {
        raiseAction.addActionListener(listener);
    }

    /**
     * Permette di ritornare il valore attuale assunto dallo Slider.
     *
     * @return Valore dello slider.
     */

    public int getSliderValue() {
        return raiseSlider.getValue();
    }

    /**
     * Permette di impostare un limite massimo al valore assumibile dallo slider.
     *
     * @param maxValue Limite massimo per lo slider
     */

    public void setMaxSliderValue(int maxValue) {
        raiseSlider.setMaximum(maxValue);
    }

    /**
     * Permette di impostare un limite minimo al valore assumibile dallo slider.
     *
     * @param minValue Limite minimo per lo slider
     */

    public void setMinSliderValue(int minValue) {
        raiseSlider.setMinimum(minValue);
    }
}
