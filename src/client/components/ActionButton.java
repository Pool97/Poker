package client.components;

import javax.swing.*;
import java.awt.*;

import static java.awt.Font.PLAIN;
import static utils.Utils.DEFAULT_FONT;

public class ActionButton extends JButton {

    public ActionButton(String action) {
        super(action);
        setComponentProperties();
    }

    private void setComponentProperties() {
        setFont(new Font(DEFAULT_FONT, PLAIN, 30));
    }
}
