package client.ui.frames;

import client.events.PlayerConnectedEvent;
import client.net.Client;
import client.net.UpdateLobbyListTask;
import server.events.EventsContainer;

import javax.swing.*;
import java.awt.*;

public class Lobby extends JFrame{
    private final static String FRAME_TITLE = "In attesa degli altri players...";

    protected String nickname;
    private String ipAddress;
    private JPanel playersList;

    public Lobby(String nickname, String avatar, String ipAddr) {
        this.ipAddress = ipAddr;
        this.nickname = nickname;
        Client client = Client.getInstance();
        client.setNickname(nickname);
        client.setParameters(ipAddr, 4040);
        client.attemptToConnect();
        initPanel();
        new UpdateLobbyListTask(this, playersList).execute();
        Client.getInstance().writeMessage(new EventsContainer(new PlayerConnectedEvent(nickname, avatar)));
        createGUI();
    }

    private void initPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBackground(new Color(0, 117, 178));
        playersList = new JPanel();
        playersList.setBackground(new Color(2, 168, 255));
        playersList.setAlignmentX(Component.CENTER_ALIGNMENT);
        playersList.add(Box.createVerticalStrut(10));
        playersList.setLayout(new BoxLayout(playersList, BoxLayout.Y_AXIS));

        JLabel hostInfo = new JLabel("Server IP: " + ipAddress, SwingConstants.CENTER);
        hostInfo.setFont(new Font("helvetica", Font.BOLD, 20));
        hostInfo.setForeground(Color.WHITE);
        hostInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel playerConnected = new JLabel("Main Event", SwingConstants.CENTER);
        playerConnected.setForeground(Color.WHITE);
        playerConnected.setFont(new Font("helvetica", Font.BOLD, 40));
        container.add(Box.createVerticalStrut(20));
        container.add(playerConnected, BorderLayout.NORTH);
        container.add(Box.createVerticalStrut(20));
        container.add(hostInfo, BorderLayout.SOUTH);
        container.add(playersList, BorderLayout.CENTER);
        add(container);
    }

    private void createGUI() {
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(FRAME_TITLE);
        setVisible(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 400);
    }
}
