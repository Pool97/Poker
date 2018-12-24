package client.ui.components;

import client.net.Client;
import client.ui.userboard.ActionButton;
import server.events.ChatMessage;
import server.events.ChatNotify;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;

import static utils.Utils.TRANSPARENT;

public class Chat extends BorderPanel {
    private JTextPane log;
    private int preferredWidth;

    public Chat(int width, String nickname){
        this.preferredWidth = width;
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
        log = new JTextPane();
        log.setFocusable(false);
        scrollPane.setViewportView(log);
    }

    public void addMessage(ChatMessage message){
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
        aset = sc.addAttribute(aset, StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
        log.setCharacterAttributes(aset, false);
        log.replaceSelection(message.getNickname() + ": " );
        aset = sc.addAttribute(aset, StyleConstants.CharacterConstants.Bold, Boolean.FALSE);
        log.setCharacterAttributes(aset, false);
        log.replaceSelection(message.toString() + "\n");
    }

    public void addNotify(ChatNotify notify){
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.RED);
        aset = sc.addAttribute(aset, StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
        log.setCharacterAttributes(aset, false);
        log.replaceSelection("Server: " + notify.getServerMessage() + "\n");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(preferredWidth, getHeight());
    }
}