package client.components;

import utils.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.Color.WHITE;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static utils.Utils.TRANSPARENT;
import static utils.Utils.getHighQualityRenderingHints;

public class ActionBoard extends JPanel {
    private final static String CHECK_FOLD_TEXT = "CHECK/FOLD";
    private final static String CALL_TEXT = "CALL";
    private final static String RAISE_TEXT = "RAISE";
    private final static int INSET = 10;
    private final static int WEIGHT = 33;
    private final static int START_PADDING = 2;
    private final static int END_PADDING = 4;
    private final static int ARC_SIZE = 30;
    private final static float STROKE_WIDTH = 4.0F;
    private ActionButton call;
    private ActionButton checkAndFold;
    private ActionButton raise;
    private RaiseSlider raiseSlider;

    public ActionBoard() {
        setComponentProperties();

        createCall();
        attachCall();

        createRaise();
        attachRaise();

        createCheckAndFold();
        attachCheckAndFold();

        createRaiseSlider();
        attachRaiseSlider();
    }

    private void setComponentProperties() {
        setLayout(new GridBagLayout());
        setBackground(TRANSPARENT);
    }

    private void createCall() {
        call = new ActionButton(CALL_TEXT);
    }

    private void attachCall() {
        add(call, new GBC(1, 1, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(0, INSET, INSET, INSET)));
    }

    private void createRaise() {
        raise = new ActionButton(RAISE_TEXT);
    }

    private void attachRaise() {
        add(raise, new GBC(2, 1, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(0, INSET, INSET, INSET)));
    }

    private void createCheckAndFold() {
        checkAndFold = new ActionButton(CHECK_FOLD_TEXT);
    }

    private void attachCheckAndFold() {
        add(checkAndFold, new GBC(0, 1, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(0, INSET, INSET, INSET)));
    }

    private void createRaiseSlider() {
        raiseSlider = new RaiseSlider();
    }

    private void attachRaiseSlider() {
        add(raiseSlider, new GBC(2, 0, 1, 1, 1, 1, CENTER, HORIZONTAL, new Insets(0, 0, 0, 20)));

    }

    public void addCallListener(ActionListener listener) {
        call.addActionListener(listener);
    }

    public void addCheckAndFoldListener(ActionListener listener) {
        checkAndFold.addActionListener(listener);
    }

    public void addRaiseListener(ActionListener listener) {
        raise.addActionListener(listener);
    }

    public int getSliderValue() {
        return raiseSlider.getValue();
    }

    public void setMaximumSliderValue(int value) {
        raiseSlider.setMaximum(value);
        raiseSlider.setMajorTickSpacing(value / 10);
    }

    public void setMinimumSliderValue(int value) {
        raiseSlider.setMinimum(value);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHints(getHighQualityRenderingHints());


        drawBackground(g2D);
        drawBorder(g2D);
    }

    private void drawBackground(Graphics2D g2D) {
        g2D.setColor(new Color(171, 39, 60));
        g2D.fillRoundRect(START_PADDING, START_PADDING, getWidth() - END_PADDING, getHeight() - END_PADDING, ARC_SIZE, ARC_SIZE);
    }

    private void drawBorder(Graphics2D g2D) {
        g2D.setStroke(new BasicStroke(STROKE_WIDTH));
        g2D.setColor(WHITE);
        g2D.drawRoundRect(START_PADDING, START_PADDING, getWidth() - END_PADDING, getHeight() - END_PADDING, ARC_SIZE, ARC_SIZE);
    }


}
