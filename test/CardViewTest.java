import graphics.CardView;

import javax.swing.*;
import java.awt.*;

public class CardViewTest {
    public static void main(String [] args){

        EventQueue.invokeLater(() ->{
            JFrame frame = new JFrame();
            frame.setLayout(new FlowLayout());
            frame.add(new CardView(new Dimension(130, 180), "2_fiori.png"));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setSize(1366, 768);
        });

    }
}
