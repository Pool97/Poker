package client.frames;

import client.components.InterfaceView;
import utils.Utils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static utils.Utils.TRANSPARENT;

public class LinuxFrame extends JFrame {
    private String nickname;
    private String avatar;
    private final static String FRAME_TITLE = "Scelta dell'interfaccia di rete";
    private JPanel netContainer;

    public LinuxFrame(String nickname, String avatar) {
        this.nickname = nickname;
        this.avatar = avatar;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(FRAME_TITLE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        createGUI();
    }

    private void createGUI() {
        setBackground(Color.BLACK);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        netContainer = new JPanel();
        netContainer.setBackground(Color.BLACK);
        netContainer.setLayout(new BoxLayout(netContainer, BoxLayout.PAGE_AXIS));
        jScrollPane.setViewportView(netContainer);
        searchForAddresses();
        add(jScrollPane);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }

    private void searchForAddresses() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (interfaces.hasMoreElements()) {
            NetworkInterface i = interfaces.nextElement();
            for (Enumeration<InetAddress> addresses = i.getInetAddresses(); addresses.hasMoreElements(); ) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    InterfaceView interfaceView = new InterfaceView(addr.getHostAddress());
                    interfaceView.addMouseListener(new MyMouseListener(interfaceView));
                    interfaceView.setAlignmentX(Component.LEFT_ALIGNMENT);
                    netContainer.add(interfaceView);
                }
            }
        }
    }

    class MyMouseListener extends MouseAdapter {
        private InterfaceView interfaceView;

        public MyMouseListener(InterfaceView interfaceView) {
            this.interfaceView = interfaceView;
        }

        public void mouseClicked(MouseEvent event) {
            dispose();
            int selectedValue = Utils.askForAChoice(new Integer[]{2, 3, 4, 5, 6}, "Inserisci il numero di partecipanti");
            new CreatorGameFrame(nickname, avatar, selectedValue, interfaceView.getIpAddress());
        }

        public void mouseEntered(MouseEvent event) {
            interfaceView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            interfaceView.setBackground(new Color(255, 255, 255, 70));
            interfaceView.setOpaque(true);
            interfaceView.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true), BorderFactory.createEmptyBorder(8, 8, 8, 0)));

        }

        public void mouseExited(MouseEvent event) {
            interfaceView.setOpaque(false);
            interfaceView.setBorder(new CompoundBorder(BorderFactory.createLineBorder(TRANSPARENT, 2, true), BorderFactory.createEmptyBorder(8, 8, 8, 0)));
        }
    }
}
