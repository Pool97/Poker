package client.components;

import utils.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.Color.WHITE;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static utils.Utils.TRANSPARENT;

public class ActionBoard extends BorderPanel {
    private final static String CHECK_TEXT = "CHECK";
    private final static String CALL_TEXT = "CALL";
    private final static String RAISE_TEXT = "RAISE";
    private final static String FOLD_TEXT = "FOLD";
    private final static int INSET = 10;
    private final static int WEIGHT = 33;
    private final static int START_PADDING = 2;
    private final static int END_PADDING = 4;
    private final static int ARC_SIZE = 30;
    private final static float STROKE_WIDTH = 4.0F;
    private ActionButton call;
    private ActionButton check;
    private ActionButton raise;
    private ActionButton fold;
    private RaiseSlider raiseSlider;

    public ActionBoard() {
        setComponentProperties();

        createCall();
        attachCall();

        createRaise();
        attachRaise();

        createCheck();
        attachCheck();

        createFold();
        attachFold();

        createRaiseSlider();
        attachRaiseSlider();
        setActionButtonsEnabled(false);
    }

    private void setComponentProperties() {
        setLayout(new GridBagLayout());
        setBackground(TRANSPARENT);
    }

    private void createCall() {
        call = new ActionButton(CALL_TEXT);
    }

    private void attachCall() {
        add(call, new GBC(1, 0, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(0, INSET, INSET, INSET)));
    }

    private void createRaise() {
        raise = new ActionButton(RAISE_TEXT);
    }

    private void createFold() {
        fold = new ActionButton(FOLD_TEXT);
    }

    private void attachRaise() {
        add(raise, new GBC(2, 0, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(0, INSET, INSET, INSET)));
    }

    private void attachFold() {
        add(fold, new GBC(3, 0, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(0, INSET, INSET, INSET)));
    }

    private void createCheck() {
        check = new ActionButton(CHECK_TEXT);
    }

    private void attachCheck() {
        add(check, new GBC(0, 0, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(0, INSET, INSET, INSET)));
    }

    private void createRaiseSlider() {
        raiseSlider = new RaiseSlider();
    }

    private void attachRaiseSlider() {
        JPanel sliderContainer = new JPanel();
        sliderContainer.setBackground(new Color(171, 39, 60));
        sliderContainer.add(raiseSlider);
        add(sliderContainer, new GBC(0, 1, 1, 1, 4, 1, CENTER, HORIZONTAL, new Insets(0, 10, 0, 10)));

    }

    public void addCallListener(ActionListener listener) {
        if (call.getActionListeners().length > 0)
            call.removeActionListener(call.getActionListeners()[0]);

        call.addActionListener(listener);
    }

    public void addCheckListener(ActionListener listener) {
        if (check.getActionListeners().length == 0)
            check.addActionListener(listener);
    }

    public void addRaiseListener(ActionListener listener) {
        if (raise.getActionListeners().length > 0)
            raise.removeActionListener(raise.getActionListeners()[0]);
        raise.addActionListener(listener);
    }

    public void addFoldListener(ActionListener listener) {
        if (fold.getActionListeners().length == 0)
            fold.addActionListener(listener);
    }

    public int getSliderValue() {
        return raiseSlider.getValue();
    }

    public void setExtremeSliderValues(int minValue, int maxValue) {
        raiseSlider.regenerateSlider(minValue, maxValue);
    }


    @Override
    protected void drawBackground(Graphics2D g2D) {
        g2D.setColor(new Color(171, 39, 60));
        g2D.fillRoundRect(START_PADDING, START_PADDING, getWidth() - END_PADDING, getHeight() - END_PADDING, ARC_SIZE, ARC_SIZE);
    }

    @Override
    protected void drawBorder(Graphics2D g2D) {
        g2D.setStroke(new BasicStroke(STROKE_WIDTH));
        g2D.setColor(WHITE);
        g2D.drawRoundRect(START_PADDING, START_PADDING, getWidth() - END_PADDING, getHeight() - END_PADDING, ARC_SIZE, ARC_SIZE);
    }

    public void setCallText(String text) {
        call.setText(CALL_TEXT + text);
    }

    public void setRaiseText(String text) {
        raise.setText(RAISE_TEXT + text);
    }

    public void setCallEnabled(boolean enable) {
        call.setEnabled(enable);
    }

    public void setCheckEnabled(boolean enable) {
        check.setEnabled(enable);
    }

    public void setRaiseEnabled(boolean enable) {
        raise.setEnabled(enable);
    }

    public void setFoldEnabled(boolean enable) {
        fold.setEnabled(enable);
    }

    public void setActionButtonsEnabled(boolean enable) {
        setCallEnabled(enable);
        setCheckEnabled(enable);
        setFoldEnabled(enable);
        setRaiseEnabled(enable);
    }
}
