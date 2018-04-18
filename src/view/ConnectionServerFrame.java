package view;

import utils.AllUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static server.socket.MainScreen.frameSetUp;
import static server.socket.MainScreen.panelBorder;

public class ConnectionServerFrame {

    private JFrame cFrame;
    public static JTextField txtIP;

    public void connessioneServer(){

        cFrame = new JFrame();

        frameSetUp(cFrame, "FrameConnesioneServer");

        JPanel panel= new JPanel();
        panelBorder(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2); // insets for all components
        c.gridx = 0; // column 0
        c.gridy = 0; // row 0
        c.ipadx = 5; // increases components width by 10 pixels
        c.ipady = 5; // increases components height by 10 pixels
        JLabel infoIP = new JLabel();
        infoIP.setText("Inserirsci IP della stanza qua:");
        panel.add(infoIP, c);

        c.gridx = 0; // column 1
        c.gridy = 1; // row 0
        // c.gridy = 0; // comment out this for reusing the obj
        c.ipadx = 0; // resets the pad to 0
        c.ipady = 0;
        txtIP = new JTextField();
        txtIP.setColumns(10);
        panel.add(txtIP, c);
        c.gridx = 0; // column 0
        c.gridy = 2; // row 1
        JButton btnSFrame = new JButton("Connect");
        panel.add(btnSFrame, c);

		/*c.gridx = 1; // column 1
		panel.add(new JButton("Support."), c);*/

        cFrame.add(panel, BorderLayout.CENTER);

        ClientFrame clF = new ClientFrame();

        btnSFrame.addActionListener(e -> {

            if(AllUtils.provaConnessione(txtIP.getText()).equals("CONNESSO")){
                try {
                    clF.cl();
                    cFrame.dispose();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        });

    }
}
