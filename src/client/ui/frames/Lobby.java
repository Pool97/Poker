package client.ui.frames;

import client.events.MatchCanStartEvent;
import client.net.Client;
import client.net.UpdateLobbyListTask;
import server.events.EventsContainer;

import javax.swing.*;
import java.awt.*;

public class Lobby extends JFrame{
    private final static String FRAME_TITLE = "In attesa degli altri players...";
    private String ipAddress;
    private JPanel playersList;

    public Lobby(String ipAddr) {
        this.ipAddress = ipAddr;

        initPanel();
        new UpdateLobbyListTask(this, playersList).execute();

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

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
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

        JButton startGame = new JButton("START");
        startGame.addActionListener(event -> Client.getInstance().writeMessage(new EventsContainer(new MatchCanStartEvent())));

        south.add(startGame);
        south.add(hostInfo);
        container.add(south, BorderLayout.SOUTH);
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
