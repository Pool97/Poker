
import graphics.PlayerModel;
import graphics.PlayerView;

import javax.swing.*;
import java.awt.*;

public class PlayerViewTest extends JFrame {
    private JPanel container;
    public PlayerViewTest(){
        container = new JPanel();
        container.add(new PlayerView(new Dimension(400, 150),new PlayerModel("HARRY POTTER", "1000000$", "SB", "FOLD", "1", "2_cuori.png", "2_quadri1.png")));
        add(container, BorderLayout.NORTH);
    }

    public static void main(String [] args){
        EventQueue.invokeLater(() ->{
            PlayerViewTest frame = new PlayerViewTest();
            frame.setSize(1366, 768);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

        });


    }

}