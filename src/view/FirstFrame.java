package view;


import javax.swing.*;
import java.awt.*;

import static server.socket.MainScreen.frameSetUp;
import static server.socket.MainScreen.panelBorder;

public class FirstFrame {
    private JFrame fFrame;
    public void firstFrame(){
        fFrame = new JFrame();

        frameSetUp(fFrame, "FrameBenvenuto");

        JPanel panel= new JPanel();
        panelBorder(panel);

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(2, 2, 2, 2); // insets for all components
        c.gridx = 0; // column 0
        c.gridy = 0; // row 0
        c.ipadx = 5; // increases components width by 10 pixels
        c.ipady = 5; // increases components height by 10 pixels
        JLabel lbl = new JLabel("Benvenuto sul test del client/server!");
        panel.add(lbl, c);

		/*c.gridx = 1; // column 1
		// c.gridy = 0; // comment out this for reusing the obj
		c.ipadx = 0; // resets the pad to 0
		c.ipady = 0;
		panel.add(new JButton("Source"), c);*/
        c.gridx = 0; // column 0
        c.gridy = 1; // row 1
        JButton btnGo = new JButton("Proviamo sto client/server vaaa!");
        panel.add(btnGo, c);

		/*c.gridx = 1; // column 1
		panel.add(new JButton("Support."), c);*/

        fFrame.add(panel, BorderLayout.CENTER);

        btnGo.addActionListener(e -> {
            SecondFrame sf = new SecondFrame();
            sf.secondFrame();

            fFrame.dispose();
        });
    }
}
