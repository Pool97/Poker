package client.components;

import utils.Utils;

import javax.swing.*;
import java.awt.*;

import static utils.Utils.EMPTY;

public class EntryPanel extends JPanel {
    private JLabel description;
    private JLabel value;

    private EntryPanel(String description, int value) {
        setComponentProperties();

        createDescription(description);
        setDescriptionProperties();
        attachDescription();

        attachSeparator();

        createValue(value);
        setValueProperties();
        attachValue();
    }

    private EntryPanel() {
        setComponentProperties();

        createDescription(EMPTY);
        setDescriptionProperties();
        attachDescription();

        attachSeparator();

        createValue(EMPTY);
        setValueProperties();
        attachValue();
    }

    public static EntryPanel createWith(String description, int value) {
        return new EntryPanel(description, value);
    }

    public static EntryPanel createEmpty() {
        return new EntryPanel();
    }

    private void setComponentProperties() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Utils.TRANSPARENT);
        setOpaque(false);
    }

    private void createDescription(String description) {
        this.description = new JLabel(description);
    }

    private void setDescriptionProperties() {
        description.setFont(Utils.getCustomFont(Font.BOLD, 25));
        description.setForeground(Color.WHITE);
    }

    private void attachDescription() {
        add(description);
    }

    private void createValue(String value) {
        this.value = new JLabel(value);
    }

    private void createValue(int value) {
        this.value = new JLabel(Integer.toString(value));
    }

    private void attachSeparator() {
        add(Box.createHorizontalGlue());
    }

    private void setValueProperties() {
        value.setFont(new Font(Utils.DEFAULT_FONT, Font.BOLD, 25));
        value.setForeground(Color.WHITE);
    }

    private void attachValue() {
        add(value);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setValue(String value) {
        this.value.setText(value);
    }

    public void setValue(int value) {
        this.value.setText(Integer.toString(value));
    }
}
