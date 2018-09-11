package client.ui.dialogs;

import client.ui.frames.WelcomeFrame;

import javax.swing.*;
import java.awt.*;

public class WinnerDialog extends JDialog {
    public WinnerDialog(String title, String description){
        setTitle(title);

        JPanel container = new JPanel();
        container.setBackground(Color.CYAN);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JLabel descr = new JLabel(description, SwingConstants.CENTER);
        descr.setFont(new Font("helvetica", Font.BOLD, 30));
        descr.setForeground(Color.WHITE);
        JButton ok = new JButton("OK");
        ok.addActionListener(event -> {
            WelcomeFrame.launchGame();
            dispose();
        });
        ok.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(descr);
        container.add(ok);
        add(container);
    }
}
