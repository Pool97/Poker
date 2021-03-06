package client.ui.frames;

import client.ui.components.ActionButton;

import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {
    private final static String FRAME_TITLE = "Benvenuto in Poker Texas!";
    private final static String ROOM_CREATION_OPTION = "Crea una stanza";
    private final static String ROOM_CONNECT_OPTION = "Connettiti";
    private final static String EXIT_OPTION = "Esci";
    private final static String ROOM_CREATION_INFO = "La stanza verrà hostata sulla porta 4040. Invia questi dati agli altri utenti.";
    private final static String MESSAGE_DIALOG_ROOM = "Creazione della stanza";

    public WelcomeFrame() {
        setLookAndFeel();
        createGUI();
    }

    public static void main(String[] args) {
        launchGame();
    }

    private void createGUI() {

        JPanel container = new JPanel();

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(178, 39, 60));
        container.add(Box.createVerticalGlue());
        ActionButton roomButton = new ActionButton(ROOM_CREATION_OPTION, new Color(0, 140, 65));
        setCustomButton(roomButton, ROOM_CREATION_OPTION.toUpperCase(), Color.GREEN);
        container.add(roomButton);
        roomButton.addActionListener(event -> {
            dispose();
            new SelectAvatarFrame(0);
        });

        ActionButton connectButton = new ActionButton(ROOM_CONNECT_OPTION, Color.ORANGE);
        setCustomButton(connectButton, ROOM_CONNECT_OPTION.toUpperCase(), Color.BLUE);
        container.add(connectButton);
        connectButton.addActionListener(event -> {
            dispose();
            new SelectAvatarFrame(1);
        });
        container.add(Box.createVerticalGlue());

        ActionButton exitButton = new ActionButton(EXIT_OPTION, new Color(3, 169, 244));
        setCustomButton(exitButton, EXIT_OPTION.toUpperCase(), new Color(3, 169, 244));
        container.add(exitButton);
        exitButton.addActionListener(event -> {
            dispose();
        });
        container.add(Box.createVerticalGlue());
        add(container);
        pack();
    }

    public static void launchGame() {
        EventQueue.invokeLater(() -> {
            try {
                WelcomeFrame frame = new WelcomeFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle(FRAME_TITLE);
                frame.setVisible(true);
                frame.setResizable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void setCustomButton(JButton button, String text, Color color) {
        button.setText(text);
        button.setFont(new Font("helvetica", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(200, 150));
        button.setMaximumSize(new Dimension(300, 150));
        //button.setBackground(color);
    }

    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {

        }


    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 500);
    }
}
