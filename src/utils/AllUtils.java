package utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class AllUtils {

    private static Socket s;

    public static void setWarningMsg(String text){
        Toolkit.getDefaultToolkit().beep();
        JOptionPane optionPane = new JOptionPane(text,JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog("Warning!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);


        /*int cake = JOptionPane.showConfirmDialog(null,
                text, "Warning!", JOptionPane.YES_NO_OPTION);

        if (cake == JOptionPane.YES_OPTION) {
            sc.MainScreen.server.dispose();
        } else if (cake == JOptionPane.NO_OPTION) {
            System.out.println("I dont like cake, no");
        }*/
    }

    public static String provaConnessione(String ip){
        try {
            s = new Socket(ip, 4040);
            System.out.println("CONNESSO!");
            return "CONNESSO";
        } catch (IOException e) {
            System.out.println("NON CONNESSO!");
            return "NON CONNESSO";
        }


    }

    public static String provaConnessione(){
        try {
            s = new Socket(InetAddress.getLocalHost().getHostAddress(), 4040);
            System.out.println("CONNESSO!");
            return "CONNESSO";
        } catch (IOException e) {
            System.out.println("NON CONNESSO!");
            return "NON CONNESSO";
        }


    }

    private static String connessione;

    public static String provaConnessioneThred(){


        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {

                    s = new Socket(InetAddress.getLocalHost().getHostAddress(), 4040);
                    System.out.println("CONNESSO!");
                    connessione = "CONNESSO";
                } catch (IOException e) {
                    System.out.println("NON CONNESSO!");
                    connessione = "NON CONNESSO";
                }
            }
        };
        thread.start();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connessione;



    }


}
