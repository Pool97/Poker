package client.ui.frames;

import client.events.MatchCanStart;
import client.net.Client;
import client.net.UpdateLobbyListTask;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Lobby extends JFrame {
    private final static String FRAME_TITLE = "In attesa degli altri players...";
    private JPanel playerList;

    public Lobby(String nickname) {

        initPanel();
        new UpdateLobbyListTask(this, playerList, nickname).execute();

        createGUI();
    }

    private void initPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(0, 117, 178));

        JLabel playerConnected = new JLabel("Lobby", SwingConstants.CENTER);
        playerConnected.setForeground(Color.WHITE);
        playerConnected.setFont(new Font("helvetica", Font.BOLD, 40));
        playerConnected.setAlignmentX(CENTER_ALIGNMENT);
        container.add(playerConnected);

        playerList = new JPanel();
        Border redBorder;
        redBorder = BorderFactory.createCompoundBorder(new EmptyBorder(5,5,5,5),
                new LineBorder(new Color(0, 117, 178), 2));
        playerList.setBorder(redBorder);
        playerList.setBackground(new Color(2, 168, 255));
        playerList.setLayout(new GridLayout(3,2));
        container.add(playerList);

        container.add(Box.createVerticalStrut(5));
        JButton startGame = new JButton("START");
        startGame.setFont(new Font("helvetica", Font.PLAIN, 16));

        startGame.addActionListener(event -> Client.getInstance().writeMessage(new MatchCanStart()));
        startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(startGame);
        container.add(Box.createVerticalStrut(5));
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
        return new Dimension(600, 600);
    }
}
