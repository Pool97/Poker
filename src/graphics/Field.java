package graphics;

import javax.swing.*;
import java.awt.*;

public class Field extends JPanel {
    public int CARD_WIDTH = 130;
    public int CARD_HEIGHT = 180;

    public Field(Dimension dimension){
        setSize(dimension);
        setLayout(new FlowLayout(FlowLayout.CENTER));
      //  add(new CardView("2", "fiori", 2));
        //add(new CardView("2", "fiori", 2));

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
