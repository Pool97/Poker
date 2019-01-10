package client.ui.dialogs;

import client.ui.components.Avatar;
import client.ui.frames.WelcomeFrame;

import javax.swing.*;
import java.awt.*;

public class LoserDialog extends JDialog {
    private final static String TITLE = "Hai perso!";
    private final static String DESCR = "RIPROVA SARAI' PIU' FORTUNATO!";
    public LoserDialog(boolean isCreator, String nick, Avatar avatar, int rank){
        setTitle(TITLE);

        JPanel containerL = new JPanel();
        containerL.setBackground(new Color(178, 5, 0));
        containerL.setLayout(new BoxLayout(containerL, BoxLayout.Y_AXIS));

        JLabel descL = new JLabel(DESCR, SwingConstants.CENTER);
        descL.setForeground(Color.WHITE);
        descL.setFont(new Font("helvetica", Font.BOLD, 15));
        descL.setAlignmentX(CENTER_ALIGNMENT);
        containerL.add(descL);

        JPanel cCenterL = new JPanel();
        cCenterL.setLayout(new GridBagLayout());
        cCenterL.setBackground(new Color(171, 39, 60));
        GridBagConstraints cP = new GridBagConstraints();

        cP.insets = new Insets(2, 2, 2, 2); // insets for all components
        cP.gridx = 0; // column 0
        cP.gridy = 0; // row 0
        cP.ipadx = 5; // increases components width by 10 pixels
        cP.ipady = 5; // increases components height by 10 pixels
        JLabel nickname = new JLabel("<html><div style='text-align: center;'>" + nick + "<br/></div></html>", SwingConstants.CENTER);
        nickname.setFont(new Font("helvetica", Font.BOLD, 20));
        nickname.setForeground(Color.WHITE);
        cCenterL.add(nickname, cP);

        cP.gridx = 0; // column 0
        cP.gridy = 1; // row 1
        cP.ipadx = 5; // increases components width by 10 pixels
        cP.ipady = 5; // increases components height by 10 pixels
        cCenterL.add(avatar, cP );

        cP.gridx = 0; // column 0
        cP.gridy = 2; // row 2
        cP.ipadx = 5; // increases components width by 10 pixels
        cP.ipady = 5; // increases components height by 10 pixels
        JLabel clss = new JLabel("<html><div style='text-align: center;'>" + "Ti sei classificato " + rank + "°" + "<br/></div></html>", SwingConstants.CENTER);
        clss.setFont(new Font("helvetica", Font.BOLD, 15));
        clss.setForeground(Color.WHITE);
        cCenterL.add(clss, cP);
        containerL.add(cCenterL);

        containerL.add(Box.createVerticalStrut(5));
        JPanel cSouthL = new JPanel();
        cSouthL.setLayout(new GridBagLayout());
        cSouthL.setBackground(new Color(178, 5, 0));
        GridBagConstraints cSP = new GridBagConstraints();

        cSP.insets = new Insets(2, 2, 2, 2); // insets for all components
        cSP.gridx = 0; // column 0
        cSP.gridy = 0; // row 0
        cSP.ipadx = 5; // increases components width by 10 pixels
        cSP.ipady = 5; // increases components height by 10 pixels
        JButton si = new JButton("OK");
        si.setFont(new Font("helvetica", Font.PLAIN, 16));
        si.setAlignmentX(Component.CENTER_ALIGNMENT);
        cSouthL.add(si, cSP);
        containerL.add(cSouthL);
        si.addActionListener(event -> {
            WelcomeFrame.launchGame();
            dispose();
        });


        add(containerL);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(350, 350);
    }
}
