package client;

import javax.swing.*;
import java.awt.*;

public class TestChat extends JFrame {
    private JTextArea textArea;

    public TestChat(){
        setLookAndFeel();
        createGUI();
    }

    private void createGUI() {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        JTextField textField = new JTextField("");
        JPanel userCommands = new JPanel();
        userCommands.setLayout(new BoxLayout(userCommands, BoxLayout.X_AXIS));
        userCommands.add(textField);
        userCommands.add(new JButton("Invia"));
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(userCommands, BorderLayout.SOUTH);
        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        add(container);

    }

    public static void main(String[] args){
        TestChat.launchGame();
    }


    public static void launchGame() {
        EventQueue.invokeLater(() -> {
            try {
                TestChat frame = new TestChat();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle("Prova Chat");
                frame.setVisible(true);
                frame.setSize(new Dimension(500, 500));
                frame.setResizable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {

        }


    }
}
