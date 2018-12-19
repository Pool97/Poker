package client.ui.userboard;

import client.ui.components.BorderPanel;
import utils.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static client.ui.userboard.ActionBoard.ActionIndexList.values;
import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static utils.Utils.TRANSPARENT;

public class ActionBoard extends BorderPanel {
    private final static int INSET = 20;
    private final static Color bgColor = new Color(171, 39, 60);
    private ArrayList<ActionButton> actionButtons;
    private RaiseSlider raiseSlider;
    private static String [] actionDescriptions = {"CALL", "CHECK", "BET",  "RAISE", "ALL IN", "FOLD"};
    public enum ActionIndexList {CALL, CHECK, BET, RAISE, ALL_IN, FOLD}

    public ActionBoard() {
        actionButtons = new ArrayList<>();
        setBoardProperties();

        addComponentsToList();
        attachComponents();

        createRaiseSlider();
        attachRaiseSlider();

        setActionButtonsEnabled(false);
    }

    private void setBoardProperties() {
        setLayout(new GridBagLayout());
        setBackground(TRANSPARENT);
    }

    private void addComponentsToList(){
        for(String description : actionDescriptions)
            actionButtons.add(new ActionButton(description, Color.ORANGE));
    }

    private void attachComponents(){
        int rowIndex = -1;
        for(int i = 0; i < values().length; i++){

            if(i % 3 == 0)
                rowIndex++;

            add(actionButtons.get(i), new GBC(i % 3, rowIndex, 33, 1, 1, 1, CENTER, HORIZONTAL,
                    new Insets(10, INSET, 5, INSET)));
        }
    }

    private void createRaiseSlider() {
        raiseSlider = new RaiseSlider();
    }

    private void attachRaiseSlider() {
        JPanel sliderContainer = new JPanel();
        sliderContainer.setBackground(bgColor);
        sliderContainer.add(raiseSlider);
        add(sliderContainer, new GBC(0, 2, 1, 1, 4, 1, CENTER, HORIZONTAL,
                new Insets(0, 15, 15, 15)));

    }

    public void addListenerTo(ActionListener listener, ActionIndexList buttonIndex){
        ActionButton button = actionButtons.get(buttonIndex.ordinal());

        if(button.getActionListeners().length > 0)
            button.removeActionListener(button.getActionListeners()[0]);

        button.addActionListener(listener);
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

        g2D.setColor(bgColor);
        g2D.fillRoundRect(strokeSize, strokeSize, getWidth() - shadowGap - strokeSize,
                getHeight() - shadowGap - strokeSize, arcs.width, arcs.height);
    }

    public void setButtonText(int actionValue, ActionIndexList buttonIndex){
        ActionButton button = actionButtons.get(buttonIndex.ordinal());
        if(actionValue != 0) {
            if (button.getText().contains("$"))
                button.setText(button.getText().substring(0, button.getText().indexOf("$")) + "$" + actionValue);
            else
                button.setText(button.getText() + " $" + actionValue);
        }
        else
            button.setText(button.getText().substring(0, button.getText().indexOf("$")));
    }

    public void setButtonEnabled(boolean enabled, ActionIndexList buttonIndex){
        actionButtons.get(buttonIndex.ordinal()).setEnabled(enabled);
    }

    public void setRaiseSliderEnabled(boolean enabled){
        raiseSlider.setEnabled(enabled);
    }

    public void setActionButtonsEnabled(boolean enabled) {
        actionButtons.forEach(button -> button.setEnabled(enabled));
    }
}
