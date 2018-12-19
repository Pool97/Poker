package client.ui.components;

import client.net.Client;
import client.ui.userboard.ActionButton;
import server.events.ChatMessage;
import server.events.ChatNotify;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static utils.Utils.TRANSPARENT;

public class Chat extends BorderPanel {
    private JTextArea log;
    
    public Chat(String nickname){
        setLayout(new BorderLayout());
        setBackground(TRANSPARENT);
        setArcDimension(new Dimension(10, 10));
        setBorder(new EmptyBorder(5,5,10,10));
        JScrollPane scrollPane = new JScrollPane();
        JTextField textField = new JTextField("");
        JPanel userCommands = new JPanel();
        userCommands.setLayout(new BoxLayout(userCommands, BoxLayout.X_AXIS));
        userCommands.add(textField);
        textField.setFont(new Font("helvetica", Font.PLAIN, 18));
        ActionButton sendMessage = new ActionButton("Invia", new Color(0, 115, 178), 18);
        sendMessage.addActionListener(event -> {
            if(!textField.getText().equals("")) {
                Client.getInstance().writeMessage(new ChatMessage(nickname, textField.getText()));
                textField.setText("");
            }
        });
        userCommands.add(sendMessage);
        add(scrollPane, BorderLayout.CENTER);
        add(userCommands, BorderLayout.SOUTH);
        log = new JTextArea();
        log.setFocusable(false);
        scrollPane.setViewportView(log);
    }

    public void addMessage(ChatMessage message){
        log.append(message.toString() + "\n");
    }

    public void addNotify(ChatNotify notify){
        log.setFont(new Font("helvetica", Font.BOLD, 14));
        log.append("Server -> " + notify.getServerMessage() + "\n");
        log.setFont(new Font("helvetica", Font.PLAIN, 14));
    }

}
