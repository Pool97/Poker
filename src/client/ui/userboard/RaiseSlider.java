package client.ui.userboard;

import client.ui.components.SliderListener;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class RaiseSlider extends JSlider {
    private final static int DEFAULT_MIN_VALUE_SLIDER = 1000;
    private final static int DEFAULT_MAX_VALUE_SLIDER = 20000;
    private Hashtable<Integer, JLabel> extremeValuesContainer;
    private JLabel minimumValue;
    private JLabel maximumValue;
    private int minValue;
    private int maxValue;

    public RaiseSlider() {
        super(JSlider.HORIZONTAL, DEFAULT_MIN_VALUE_SLIDER, DEFAULT_MAX_VALUE_SLIDER, DEFAULT_MIN_VALUE_SLIDER);
        setComponentListeners();
        setComponentProperties();
        createExtremeValuesContainer();
        createMinimumValue(DEFAULT_MIN_VALUE_SLIDER);
        setMinimumValueProperties();
        createMaximumValue(DEFAULT_MAX_VALUE_SLIDER);
        setMaximumValueProperties();
        attachExtremeValues();
        attachExtremeValuesContainer();
    }

    private void setComponentProperties() {
        setSpacing(2, 5000);
        setPaintTicks(true);
        setPaintLabels(true);
        setBackground(new Color(171, 39, 60));
    }

    private void setSpacing(int minorSpacing, int maxSpacing) {
        //setMinorTickSpacing(minorSpacing);
        //setMajorTickSpacing(5000);
    }

    private void createExtremeValuesContainer() {
        extremeValuesContainer = new Hashtable<>();
    }

    private void createMinimumValue(int value) {
        minimumValue = new JLabel(Integer.toString(value));
    }

    private void setMinimumValueProperties() {
        minimumValue.setFont(new Font(Utils.DEFAULT_FONT, Font.BOLD, 12));
        minimumValue.setForeground(Color.WHITE);
    }

    private void createMaximumValue(int value) {
        maximumValue = new JLabel(Integer.toString(value));
    }

    private void setMaximumValueProperties() {
        maximumValue.setFont(new Font(Utils.DEFAULT_FONT, Font.BOLD, 12));
        maximumValue.setForeground(Color.WHITE);
    }

    private void attachExtremeValues() {
        extremeValuesContainer.put(DEFAULT_MIN_VALUE_SLIDER, minimumValue);
        extremeValuesContainer.put(DEFAULT_MAX_VALUE_SLIDER, maximumValue);
    }

    private void attachExtremeValuesContainer() {
        setLabelTable(extremeValuesContainer);
    }

    private void setComponentListeners() {
        SliderListener listener = new SliderListener();
        addMouseMotionListener(listener);
        addMouseListener(listener);
    }

    private void setMinimumLabel(int value){
        minimumValue.setText(Integer.toString(value));
    }

    private void setMaximumLabel(int value){
        maximumValue.setText(Integer.toString(value));
    }

    public void setMaximumValue(int value) {
        this.maxValue = value;
    }

    public void setMinimumValue(int value) {
        this.minValue = value;
    }

    public void regenerateSlider() {
        setPaintLabels(false);
        setLabelTable(null);

        //setSpacing(minValue/ 10, maxValue / 10);
        setMinimumLabel(minValue);
        setMaximumLabel(maxValue);

        extremeValuesContainer = new Hashtable<>();
        createMinimumValue(minValue);
        setMinimumValueProperties();

        createMaximumValue(maxValue);
        setMaximumValueProperties();

        extremeValuesContainer.put(minValue, minimumValue);
        extremeValuesContainer.put(maxValue, maximumValue);

        setLabelTable(extremeValuesContainer);

        setMinimum(minValue);
        setMaximum(maxValue);

        setValue(minValue);
        setPaintTicks(true);
        setPaintLabels(true);
    }


}
