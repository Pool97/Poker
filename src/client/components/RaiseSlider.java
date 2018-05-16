package client.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class RaiseSlider extends JSlider {
    private final static int DEFAULT_MIN_VALUE_SLIDER = 0;
    private final static int DEFAULT_MAX_VALUE_SLIDER = 100;
    private Hashtable<Integer, JLabel> extremeValuesContainer;
    private JLabel minimumValue;
    private JLabel maximumValue;

    public RaiseSlider() {
        super(JSlider.HORIZONTAL, DEFAULT_MIN_VALUE_SLIDER, DEFAULT_MAX_VALUE_SLIDER, 0);
        setComponentListeners();
        setComponentProperties();
        createExtremeValuesContainer();
        createMinimumValue();
        setMinimumValueProperties();
        createMaximumValue();
        setMaximumValueProperties();
        attachExtremeValues();
        attachExtremeValuesContainer();
    }

    private void setComponentProperties() {
        setMinorTickSpacing(DEFAULT_MIN_VALUE_SLIDER);
        setMajorTickSpacing(DEFAULT_MAX_VALUE_SLIDER / 10);
        setPaintTicks(true);
        setPaintLabels(true);
    }

    private void createExtremeValuesContainer() {
        extremeValuesContainer = new Hashtable<>();
    }

    private void createMinimumValue() {
        minimumValue = new JLabel(Integer.toString(DEFAULT_MIN_VALUE_SLIDER));
    }

    private void setMinimumValueProperties() {
        minimumValue.setFont(new Font(Utils.DEFAULT_FONT, Font.PLAIN, 20));
        minimumValue.setForeground(Color.WHITE);
    }

    private void createMaximumValue() {
        maximumValue = new JLabel(Integer.toString(DEFAULT_MAX_VALUE_SLIDER));
    }

    private void setMaximumValueProperties() {
        maximumValue.setFont(new Font(Utils.DEFAULT_FONT, Font.PLAIN, 20));
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
}
