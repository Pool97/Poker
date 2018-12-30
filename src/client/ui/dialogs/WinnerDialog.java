package client.ui.dialogs;

import client.ui.components.Avatar;
import client.ui.frames.WelcomeFrame;

import javax.swing.*;
import java.awt.*;

public class WinnerDialog extends JDialog {
    private final static String TITLE = "Hai vinto";
    private final static String DESCR = "Complimenti! Hai vinto";

    public WinnerDialog(String nick, String chips, Avatar avatar){
        setTitle(TITLE);

        JPanel containerW = new JPanel();
        containerW.setBackground(new Color(12, 255,0));
        containerW.setLayout(new BoxLayout(containerW, BoxLayout.Y_AXIS));

        JLabel descW = new JLabel(DESCR, SwingConstants.CENTER);
        descW.setForeground(Color.WHITE);
        descW.setFont(new Font("helvetica", Font.BOLD, 20));
        descW.setAlignmentX(CENTER_ALIGNMENT);
        containerW.add(descW);

        JPanel cW = new JPanel();
        cW.setLayout(new GridBagLayout());
        cW.setBackground(new Color(110, 255,0));
        GridBagConstraints cP = new GridBagConstraints();

        cP.insets = new Insets(2, 2, 2, 2); // insets for all components
        cP.gridx = 0; // column 0
        cP.gridy = 0; // row 0
        cP.ipadx = 5; // increases components width by 10 pixels
        cP.ipady = 5; // increases components height by 10 pixels
        JLabel nickname = new JLabel("<html><div style='text-align: center;'>" + nick + "<br/></div></html>", SwingConstants.CENTER);
        nickname.setFont(new Font("helvetica", Font.BOLD, 20));
        nickname.setForeground(Color.WHITE);
        cW.add(nickname, cP);

        cP.gridx = 0; // column 0
        cP.gridy = 1; // row 1
        cP.ipadx = 5; // increases components width by 10 pixels
        cP.ipady = 5; // increases components height by 10 pixels
        cW.add(avatar, cP);

        cP.gridx = 0; // column 0
        cP.gridy = 2; // row 2
        cP.ipadx = 5; // increases components width by 10 pixels
        cP.ipady = 5; // increases components height by 10 pixels
        JLabel cps = new JLabel("<html><div style='text-align: center;'>" + "Hai vinto " + chips + "<br/></div></html>", SwingConstants.CENTER);
        cps.setFont(new Font("helvetica", Font.BOLD, 20));
        cps.setForeground(Color.WHITE);
        cW.add(cps, cP);
        containerW.add(cW);


        containerW.add(Box.createVerticalStrut(5));
        JPanel cSouthW = new JPanel();
        cSouthW.setLayout(new GridBagLayout());
        cSouthW.setBackground(new Color(12, 255,0));
        GridBagConstraints cSP = new GridBagConstraints();

        cSP.insets = new Insets(2, 2, 2, 2); // insets for all components
        cSP.gridx = 0; // column 0
        cSP.gridy = 0; // row 0
        cSP.ipadx = 5; // increases components width by 10 pixels
        cSP.ipady = 5; // increases components height by 10 pixels
        JButton ok = new JButton("OK");
        ok.setFont(new Font("helvetica", Font.PLAIN, 16));
        ok.setAlignmentX(Component.CENTER_ALIGNMENT);
        cSouthW.add(ok, cSP);
        containerW.add(cSouthW);
        ok.addActionListener(event -> {
            WelcomeFrame.launchGame();
            dispose();
        });


        add(containerW);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(350, 350);
    }
}
