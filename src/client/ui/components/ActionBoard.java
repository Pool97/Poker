package client.ui.components;

import utils.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static client.ui.components.ActionBoard.ActionIndexList.*;
import static java.awt.GridBagConstraints.*;
import static utils.Utils.TRANSPARENT;

public class ActionBoard extends BorderPanel {
    private final static int INSET = 10;
    private final static Color bgColor = new Color(171, 39, 60);
    private ArrayList<ActionButton> actionButtons;
    private RaiseSlider raiseSlider;
    private static String[] actionDescriptions = {"CALL", "CHECK", "BET", "FOLD", "RAISE", "x3", "1/2 POT", "POT", "ALL IN"};

    public enum ActionIndexList {CALL, CHECK, BET, FOLD, RAISE, THREE_BET, HALF_POT, POT, ALL_IN}

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
        JPanel actionsContainer = new JPanel();
        actionsContainer.setBackground(bgColor);
        for (int i = 0; i < 4; i++)
            actionsContainer.add(actionButtons.get(i));
        add(actionsContainer, new GBC(0, rowIndex, 1, 1, 4, 1, CENTER, NONE,
                new Insets(10, INSET, 5, INSET)));
        JPanel raiseContainer = new JPanel();
        raiseContainer.setBackground(bgColor);
        raiseContainer.add(actionButtons.get(RAISE.ordinal()));
        raiseContainer.add(actionButtons.get(THREE_BET.ordinal()));
        raiseContainer.add(actionButtons.get(HALF_POT.ordinal()));
        raiseContainer.add(actionButtons.get(POT.ordinal()));
        raiseContainer.add(actionButtons.get(ALL_IN.ordinal()));
        add(raiseContainer, new GBC(0, 3, 1, 1, 4, 1, CENTER, NONE,
                new Insets(5, INSET, 20, INSET)));
    }

    private void createRaiseSlider() {
        raiseSlider = new RaiseSlider();
    }

    private void attachRaiseSlider() {
        JPanel sliderContainer = new JPanel();
        sliderContainer.setBackground(bgColor);
        sliderContainer.add(raiseSlider);
        add(sliderContainer, new GBC(0, 2, 1, 1, 4, 1, CENTER, HORIZONTAL,
                new Insets(0, 15, 7, 15)));

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

    public void setSpecialRaiseValue(int value, ActionIndexList index) {
        if (value > 0) {
            ActionButton specialRaise = actionButtons.get(index.ordinal());
            specialRaise.setEnabled(true);
            if (specialRaise.getActionListeners().length > 0)
                specialRaise.removeActionListener(specialRaise.getActionListeners()[0]);
            specialRaise.addActionListener(event -> raiseSlider.setValue(value));
        }
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
                button.setText(button.getText().substring(0, button.getText().indexOf("$") - 1) + " $" + actionValue);
            else
                button.setText(button.getText() + " $" + actionValue);
        } else if (button.getText().contains("$"))
            button.setText(button.getText().substring(0, button.getText().indexOf("$") - 1));
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
