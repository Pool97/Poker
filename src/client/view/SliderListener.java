package client.view;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

public class SliderListener extends MouseAdapter {
    private JWindow tooltip;
    private JLabel label = new JLabel("", SwingConstants.CENTER);
    private Dimension size = new Dimension(120, 50);
    private int prevValue = -1;

    public SliderListener() {
        super();
        tooltip = new JWindow();
        tooltip.setBackground(Utils.TRANSPARENT);
        JPanel container = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHints(qualityHints);
                g2.setStroke(new BasicStroke(4.0F));

                g2.setColor(Color.BLACK);
                g2.fillRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 20, 20);
                g2.setColor(new Color(255, 215, 0));
                g2.drawRoundRect(5, 5, getWidth() - 8, getHeight() - 8, 20, 20);


            }
        };
        container.setBackground(Utils.TRANSPARENT);
        container.setLayout(new BorderLayout());
        label.setOpaque(false);
        label.setForeground(new Color(255, 215, 0));
        label.setFont(new Font("helvetica", Font.BOLD, 18));
        container.add(label);
        tooltip.add(container);

        tooltip.setSize(size);
    }

    public void setPopup(MouseEvent mouseEvent) {
        JSlider slider = (JSlider) mouseEvent.getComponent();
        int intValue = slider.getValue();
        if (prevValue != intValue) {
            final DecimalFormat db = new DecimalFormat();
            label.setText(db.format(slider.getValue()));
            Point pt = mouseEvent.getPoint();
            pt.y = -size.height;
            SwingUtilities.convertPointToScreen(pt, mouseEvent.getComponent());
            pt.translate(-size.width / 2, 0);
            tooltip.setLocation(pt);
        }
        prevValue = intValue;
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        tooltip.setVisible(true);
        setPopup(e);
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        tooltip.setVisible(false);
    }
}
