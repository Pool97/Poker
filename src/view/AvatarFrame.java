package view;

import javax.swing.*;
import java.awt.*;

import static server.socket.MainScreen.frameSetUp;
import static server.socket.MainScreen.panelBorder;

public class AvatarFrame {

    public static JFrame aFrame;
    public static JTextField txtInput;

    public void avatarFrame() {

        aFrame = new JFrame();

        frameSetUp(aFrame, "FrameCreazioneAvatar");

        JPanel panel= new JPanel();
        panelBorder(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2); // insets for all components
        c.gridx = 0; // column 0
        c.gridy = 0; // row 0
        c.ipadx = 5; // increases components width by 10 pixels
        c.ipady = 5; // increases components height by 10 pixels
        JLabel infoAvatar = new JLabel();
        infoAvatar.setText("Inserirsci il nick qua:");
        panel.add(infoAvatar, c);

        c.gridx = 0; // column 1
        c.gridy = 1; // row 0
        // c.gridy = 0; // comment out this for reusing the obj
        c.ipadx = 0; // resets the pad to 0
        c.ipady = 0;
        txtInput = new JTextField();
        txtInput.setColumns(10);
        panel.add(txtInput, c);
        c.gridx = 0; // column 0
        c.gridy = 2; // row 1
        JButton btnSFrame = new JButton("Save It");
        panel.add(btnSFrame, c);

		/*c.gridx = 1; // column 1
		panel.add(new JButton("Support."), c);*/

        aFrame.add(panel, BorderLayout.CENTER);

        SecondFrame sf = new SecondFrame();

        btnSFrame.addActionListener(e -> {

            sf.secondFrame();

            aFrame.dispose();
        });
    }
}
