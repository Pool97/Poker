package client.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;

public class InterfaceView extends JPanel {
    private JLabel icon;
    private JLabel ipAddress;

    public InterfaceView(String ipAddress) {
        setBackground(new Color(238, 96, 2));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        this.ipAddress = new JLabel(ipAddress, SwingConstants.LEFT);
        this.ipAddress.setFont(new Font("helvetica", Font.PLAIN, 20));
        this.ipAddress.setForeground(Color.WHITE);
        icon = new JLabel();
        icon.setHorizontalAlignment(SwingConstants.LEFT);
        icon.setIcon(new ImageIcon(Utils.loadImage("ethernet.png", new Dimension(48, 48))));
        add(icon);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(this.ipAddress);
        add(Box.createHorizontalGlue());
    }

    public String getIpAddress() {
        return ipAddress.getText();
    }
}
