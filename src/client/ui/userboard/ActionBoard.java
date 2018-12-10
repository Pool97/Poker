package client.ui.userboard;

import client.ui.components.BorderPanel;
import utils.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static utils.Utils.TRANSPARENT;

public class ActionBoard extends BorderPanel {
    private final static String CHECK_TEXT = "CHECK";
    private final static String CALL_TEXT = "CALL";
    private final static String RAISE_TEXT = "RAISE";
    private final static String BET_TEXT = "BET";
    private final static String FOLD_TEXT = "FOLD";
    private final static int INSET = 20;
    private final static int WEIGHT = 33;
    private ActionButton call;
    private ActionButton check;
    private ActionButton raiseAndBet;
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
        call = new ActionButton(CALL_TEXT, Color.ORANGE);
    }

    private void attachCall() {
        add(call, new GBC(1, 0, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(15, INSET, INSET, INSET)));
    }

    private void createRaise() {
        raiseAndBet = new ActionButton(RAISE_TEXT, Color.ORANGE);
    }

    private void createFold() {
        fold = new ActionButton(FOLD_TEXT, Color.ORANGE);
    }

    private void attachRaise() {
        add(raiseAndBet, new GBC(2, 0, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(15, INSET, INSET, INSET)));
    }

    private void attachFold() {
        add(fold, new GBC(3, 0, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(15, INSET, INSET, INSET)));
    }

    private void createCheck() {
        check = new ActionButton(CHECK_TEXT, Color.ORANGE);
    }

    private void attachCheck() {
        add(check, new GBC(0, 0, WEIGHT, 1, 1, 1, CENTER, HORIZONTAL,
                new Insets(15, INSET, INSET, INSET)));
    }

    private void createRaiseSlider() {
        raiseSlider = new RaiseSlider();
    }

    private void attachRaiseSlider() {
        JPanel sliderContainer = new JPanel();
        sliderContainer.setBackground(new Color(171, 39, 60));
        sliderContainer.add(raiseSlider);
        add(sliderContainer, new GBC(0, 1, 1, 1, 4, 1, CENTER, HORIZONTAL, new Insets(0, 15, 15, 15)));

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
        if (raiseAndBet.getActionListeners().length > 0)
            raiseAndBet.removeActionListener(raiseAndBet.getActionListeners()[0]);
        raiseAndBet.addActionListener(listener);
    }

    public void addFoldListener(ActionListener listener) {
        if (fold.getActionListeners().length == 0)
            fold.addActionListener(listener);
    }

    public int getSliderValue() {
        return raiseSlider.getValue();
    }

    public void setMinSlider(int minValue){
        raiseSlider.setMinimumValue(minValue);
    }

    public void setMaxSlider(int maxValue){
        raiseSlider.setMaximumValue(maxValue);
    }
    public void refreshSlider() {
        raiseSlider.regenerateSlider();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        g2D.setColor(new Color(171, 39, 60));
        //Draws the rounded opaque panel with borders.
        g2D.fillRoundRect(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                getHeight() - shadowGap - strokeSize, arcs.width, arcs.height);
    }

    @Override
    protected void drawBackground(Graphics2D g2D) {

    }

    @Override
    protected void drawBorder(Graphics2D g2D, Color color) {

    }

    public void setCallText(String text) {
        call.setText(CALL_TEXT + text);
    }

    public void setRaiseText(String text) {
        raiseAndBet.setText(RAISE_TEXT + " $" + text);
    }

    public void setBetText(String text) {
        raiseAndBet.setText(BET_TEXT + " $" + text);
    }
    public void setCallEnabled(boolean enable) {
        call.setEnabled(enable);
    }

    public void setCheckEnabled(boolean enable) {
        check.setEnabled(enable);
    }

    public void setRaiseEnabled(boolean enable) {
        raiseAndBet.setEnabled(enable);
    }

    public void setFoldEnabled(boolean enable) {
        fold.setEnabled(enable);
    }

    public void setRaiseSliderEnabled(boolean enable){
        raiseSlider.setEnabled(enable);
    }

    public void setActionButtonsEnabled(boolean enable) {
        setCallEnabled(enable);
        setCheckEnabled(enable);
        setFoldEnabled(enable);
        setRaiseEnabled(enable);
    }
}
