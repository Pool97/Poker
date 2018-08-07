package client.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PokerDialog extends JDialog {
    private final static String INSERT_NICKNAME = "NICKNAME";
    private final static String DIALOG_TITLE = "Configurazione del match";
    protected ActionButton confirmAction;
    protected ActionButton cancelAction;
    protected PokerTextField nicknameField;

    public PokerDialog() {
        setTitle(DIALOG_TITLE);
        createConfirmButton();
        createCancelButton();
        setConfirmButtonProperties();
        setCancelButtonProperties();

        createNicknameTextField();
    }

    private void createConfirmButton() {
        confirmAction = new ActionButton("OK", Color.WHITE, 18);
    }

    private void createCancelButton() {
        cancelAction = new ActionButton("CANCEL", Color.WHITE, 18);
    }

    private void createNicknameTextField() {
        nicknameField = new PokerTextField(INSERT_NICKNAME);
    }

    private void setConfirmButtonProperties() {
        confirmAction.setForeground(Color.BLACK);
    }

    private void setCancelButtonProperties() {
        cancelAction.setForeground(Color.BLACK);
    }

    public void addConfirmButtonListener(ActionListener actionListener) {
        confirmAction.addActionListener(actionListener);
    }

    public void addCancelButtonListener(ActionListener actionListener) {
        cancelAction.addActionListener(actionListener);
    }
}
