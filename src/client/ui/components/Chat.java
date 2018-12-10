package client.ui.components;

import client.net.Client;
import server.events.ChatMessage;

import javax.swing.*;
import java.awt.*;

public class Chat extends JPanel {
    private JTextArea log;
    
    public Chat(){
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        JTextField textField = new JTextField("");
        JPanel userCommands = new JPanel();
        userCommands.setLayout(new BoxLayout(userCommands, BoxLayout.X_AXIS));
        userCommands.add(textField);
        JButton sendMessage = new JButton("Invia");
        sendMessage.addActionListener(event -> {
            if(!textField.getText().equals(""))
                Client.getInstance().writeMessage(new ChatMessage("Stucazz", textField.getText()));
        });
        userCommands.add(sendMessage);
        add(scrollPane, BorderLayout.CENTER);
        add(userCommands, BorderLayout.SOUTH);
        log = new JTextArea();
        log.setFocusable(false);
        scrollPane.setViewportView(log);
    }

    public void addMessage(ChatMessage message){
        log.append(message.getNickname() + "->" + message.getMessage());
    }

}
