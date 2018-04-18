package view;

import server.socket.ServerSocketManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

import static view.AvatarFrame.txtInput;
import static view.SecondFrame.sFrame;

public class ServerFrame {

    public static JFrame server;
    public static JTextArea txtLogSv;

    public void sv() throws IOException {
        String hostName = null;
        try{
            hostName = txtInput.getText();

            server = new JFrame();
            server.setBounds(100, 100, 450, 300);
            server.setTitle("SERVER HOST " + "BY " + hostName);
            server.setVisible(true);

            JScrollPane scrollPane = new JScrollPane();
            server.add(scrollPane, BorderLayout.CENTER);

            txtLogSv = new JTextArea();
            scrollPane.setViewportView(txtLogSv);

            new Thread(new ServerSocketManager()).start();
            //new Thread(new MultithreaddSocketServer()).start();

            ClientFrame clF = new ClientFrame();
            clF.cl();
            JOptionPane.showMessageDialog(sFrame,"Dallo ai tuoi amici per connettersi alla tua stanza!!\n" + "IP Stanza: " + InetAddress.getLocalHost().getHostAddress());

        }catch(NullPointerException e){
            String msg = "<html><font color=\"#cc0000\">Allora sei proprio Coglione, te l'avevo pure detto di creare l'Avatar prima!</font></html>";
            JOptionPane.showMessageDialog(sFrame, msg);
        }



    }
}
