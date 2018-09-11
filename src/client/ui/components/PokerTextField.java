package client.ui.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PokerTextField extends JTextField implements FocusListener {
    private String tooltipText;
    private String mem;

    public PokerTextField(String tooltipText) {
        this.tooltipText = tooltipText;
        mem = "";
        setText(tooltipText);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setOpaque(false);
        setColumns(20);
        setBackground(new Color(0, 201, 255));
        setForeground(Color.WHITE);
        setFont(new Font("helvetica", Font.PLAIN, 20));
        setHorizontalAlignment(SwingConstants.CENTER);
        addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (mem.equals(tooltipText))
            setText("");
        else
            setText(mem);
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (getText().equals(Utils.EMPTY)) {
            setText(tooltipText);
        } else if (!getText().equals(tooltipText)) {
            mem = getText();
        }
    }
}
