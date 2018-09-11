package client.ui.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import static java.awt.Font.BOLD;
import static javax.swing.SwingConstants.CENTER;
import static utils.Utils.*;

public class SliderListener extends MouseAdapter {
    private JWindow tooltip;
    private JLabel sliderValue;
    private JPanel container;
    private Dimension windowSize = new Dimension(120, 50);
    private int previousValue = -1;

    public SliderListener() {
        super();
        createTooltip();
        setTooltipProperties();

        createContainer();
        setContainerProperties();
        attachContainer();

        createSliderValue();
        attachSliderValue();
        setSliderValueProperties();
    }

    private void createTooltip() {
        tooltip = new JWindow();
    }

    private void setTooltipProperties() {
        tooltip.setBackground(TRANSPARENT);
        tooltip.setSize(windowSize);
    }

    private void createContainer() {
        container = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2D = (Graphics2D) g;
                g2D.setRenderingHints(Utils.getHighQualityRenderingHints());

                drawBackground(g2D);
                drawBorder(g2D);
            }

            public void drawBorder(Graphics2D g2D) {
                g2D.setStroke(new BasicStroke(4.0F));
                g2D.setColor(new Color(255, 215, 0));
                g2D.drawRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 20, 20);
            }

            public void drawBackground(Graphics2D g2D) {
                g2D.setColor(Color.BLACK);
                g2D.fillRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 20, 20);
            }
        };
    }

    private void setContainerProperties() {
        container.setLayout(new BorderLayout());
        container.setBackground(TRANSPARENT);
    }

    private void attachContainer() {
        tooltip.add(container);
    }

    private void createSliderValue() {
        sliderValue = new JLabel(EMPTY, CENTER);
    }

    private void attachSliderValue() {
        container.add(sliderValue);
    }

    private void setSliderValueProperties() {
        sliderValue.setOpaque(false);
        sliderValue.setForeground(new Color(255, 215, 0));
        sliderValue.setFont(new Font(DEFAULT_FONT, BOLD, 18));
    }

    private void updateTooltipProperties(MouseEvent mouseEvent) {
        JSlider slider = (JSlider) mouseEvent.getComponent();
        int newValue = slider.getValue();

        if (isUpdatedValue(newValue)) {
            sliderValue.setText(formatValue(newValue));
            Point mouseLocation = mouseEvent.getPoint();
            tooltip.setLocation(calculateNewTooltipLocation(mouseLocation, slider));
        }

        previousValue = newValue;
    }

    private boolean isUpdatedValue(int value) {
        return previousValue != value;
    }

    private String formatValue(int value) {
        DecimalFormat db = new DecimalFormat();
        return db.format(value);
    }

    private Point calculateNewTooltipLocation(Point mouseLocation, JSlider slider) {
        mouseLocation.y = -windowSize.height;
        SwingUtilities.convertPointToScreen(mouseLocation, slider);
        mouseLocation.translate(-windowSize.width / 2, 0);
        return mouseLocation;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        tooltip.setVisible(true);
        updateTooltipProperties(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        tooltip.setVisible(false);
    }
}
