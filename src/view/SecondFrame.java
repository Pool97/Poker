package view;

import utils.AllUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static server.socket.MainScreen.*;

public class SecondFrame {

    public static JFrame sFrame;

    public void secondFrame(){

        sFrame = new JFrame();

        frameSetUp(sFrame, "FrameMenu");

        JPanel panel= new JPanel();
        panelBorder(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2); // insets for all components
        c.gridx = 0; // column 0
        c.gridy = 0; // row 0
        c.ipadx = 5; // increases components width by 10 pixels
        c.ipady = 5; // increases components height by 10 pixels
        JLabel infoAvatar = new JLabel();
        infoAvatar.setText("Crea l'Avatar prima di Hostare, grazie!!");
        Color myColor = Color.decode("#cc0000");
        infoAvatar.setForeground (myColor);
        panel.add(infoAvatar, c);
		/*c.gridx = 1; // column 1
		// c.gridy = 0; // comment out this for reusing the obj
		c.ipadx = 0; // resets the pad to 0
		c.ipady = 0;
		panel.add(new JButton("Source"), c);*/

        c.gridx = 0; // column 0
        c.gridy = 1; // row 1
        JButton btnAvatar = new JButton("Crea l'avatar");
        panel.add(btnAvatar, c);

        c.gridx = 0; // column 0
        c.gridy = 2; // row 1
        JButton btnHost= new JButton("Crea la Stanza");
        panel.add(btnHost, c);

        c.gridx = 0; // column 0
        c.gridy = 3; // row 1
        JButton btnClient= new JButton("Connettiti a una stanza");
        panel.add(btnClient, c);

		/*c.gridx = 1; // column 1
		panel.add(new JButton("Support."), c);*/

        sFrame.add(panel, BorderLayout.CENTER);

        btnAvatar.addActionListener(e -> {
            AvatarFrame af = new AvatarFrame();
            af.avatarFrame();
            sFrame.dispose();
        });

    ServerFrame svF = new ServerFrame();
    ClientFrame clF = new ClientFrame();

        if(connesso == true){
            btnHost.setEnabled(false);
        }else{
            btnHost.addActionListener(e -> {

                if(connesso == true){
                    try {
                        clF.cl();
                        sFrame.dispose();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else{
                    try {
                        svF.sv();
                        sFrame.dispose();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            });
        }

        ConnectionServerFrame cSF = new ConnectionServerFrame();

        btnClient.addActionListener(e -> {
            if(AllUtils.provaConnessione().equals("CONNESSO")){
                try {
                    clF.cl();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                sFrame.dispose();
            }else{
                cSF.connessioneServer();
                sFrame.dispose();
            }


        });



    }
}
