package client.ui.frames;

import client.ui.components.Avatar;
import client.ui.dialogs.PlayerDialog;
import client.ui.userboard.ActionButton;
import server.controller.ServerManager;

import javax.swing.*;
import java.awt.*;

public class SelectModeFrame extends JFrame {
    private final static String ROOM_CREATION_OPTION = "NO LIMIT MODE";
    private final static String FIXED_LIMIT_MODE = "FIXED LIMIT MODE";

    public SelectModeFrame(Avatar avatar){
        createGUI(avatar);
    }

    private void createGUI(Avatar avatar) {

        JPanel container = new JPanel();

        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setBackground(new Color(178, 39, 60));
        container.add(Box.createHorizontalGlue());
        ActionButton noLimitMode = new ActionButton(ROOM_CREATION_OPTION, new Color(0, 140, 65));
        setCustomButton(noLimitMode, ROOM_CREATION_OPTION.toUpperCase(), Color.GREEN);
        container.add(noLimitMode);
        noLimitMode.addActionListener(event -> {
            new Thread(new ServerManager()).start();
            dispose();
            PlayerDialog dialog = new PlayerDialog(avatar, false);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setFocusOnButton();
        });
        container.add(Box.createHorizontalStrut(20));
        ActionButton fixedLimitMode = new ActionButton(FIXED_LIMIT_MODE, Color.ORANGE);
        setCustomButton(fixedLimitMode, FIXED_LIMIT_MODE.toUpperCase(), Color.BLUE);
        container.add(fixedLimitMode);
        fixedLimitMode.addActionListener(event -> {
            new Thread(new ServerManager()).start();
            dispose();
            PlayerDialog dialog = new PlayerDialog(avatar, true);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setFocusOnButton();
        });
        container.add(Box.createHorizontalGlue());

        add(container);
        pack();
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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 500);
    }
}
