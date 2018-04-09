package graphics;

import main.Card;

import javax.swing.*;
import java.awt.*;

public class Field extends JPanel {
    public int CARD_WIDTH = 130;
    public int CARD_HEIGHT = 180;

    public Field(Dimension dimension){
        setSize(dimension);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        for(int i = 0; i < 5; i++)
            add(new Card());

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1300, 200);
    }
}
