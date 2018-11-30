package client.ui.dialogs;

import client.ui.frames.WelcomeFrame;

import javax.swing.*;
import java.awt.*;

public class LoserDialog extends JDialog {
    public LoserDialog(String title, String description, boolean isCreator){
        setTitle(title);

        JPanel container = new JPanel();
        container.setBackground(Color.CYAN);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JLabel descr = new JLabel(description, SwingConstants.CENTER);
        descr.setFont(new Font("helvetica", Font.BOLD, 30));
        descr.setForeground(Color.WHITE);
        JButton ok = new JButton("Sì");
        ok.addActionListener(event -> {
            dispose();
        });
        ok.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(descr);
        container.add(ok);
        if(!isCreator){
            JButton no = new JButton("No");
            no.addActionListener(event -> {
                WelcomeFrame.launchGame();
                dispose();
            });
            container.add(no);
        }

        add(container);
    }
}
