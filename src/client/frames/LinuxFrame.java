package client.frames;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.Enumeration;

public class LinuxFrame extends JFrame {
    private final static String FRAME_TITLE = "Scelta dell'interfaccia di rete";
    private JPanel netContainer;

    public LinuxFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(FRAME_TITLE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        createGUI();
    }

    private void createGUI() {
        JScrollPane jScrollPane = new JScrollPane();
        netContainer = new JPanel();
        netContainer.setLayout(new BoxLayout(netContainer, BoxLayout.Y_AXIS));
        jScrollPane.setViewportView(netContainer);
        try {
            searchForAddresses(true, false);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        add(jScrollPane);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }

    private void searchForAddresses(boolean preferIpv4, boolean preferIPv6) throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface i = interfaces.nextElement();
            for (Enumeration<InetAddress> addresses = i.getInetAddresses(); addresses.hasMoreElements(); ) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    netContainer.add(new JLabel("Address: " + addr.getHostAddress()));
                }
                if (addr instanceof Inet6Address) {
                }
            }
        }
    }
}
