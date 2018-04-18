package server.socket;

import utils.AllUtils;
import view.FirstFrame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

public class MainScreen {
	private String ipHost ;

	public static JTextField txtInputNum;

	public static Boolean connesso;

	/**
	 * Launch the application.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					new MainScreen();
					//window.fFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private MainScreen() {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		connection();
		FirstFrame ff = new FirstFrame();
		ff.firstFrame();
	}


	public static void frameSetUp(JFrame f, String str){

		f.setBounds(100, 100, 450, 300);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(new BorderLayout(0, 0));
		//frame.setResizable(true);
		//frame.setLocationByPlatform( true );
		f.setTitle(str);
		f.revalidate();
		f.repaint();
		f.setVisible(true);

		JPanel north = new JPanel();
		JPanel south = new JPanel();
		JPanel east = new JPanel();
		JPanel west = new JPanel();

		JLabel n = new JLabel();
		JLabel s = new JLabel();
		JLabel e = new JLabel();
		JLabel w = new JLabel();

		n.setText(" ");
		s.setText(" ");
		e.setText(" ");
		w.setText(" ");

		north.add(n);
		south.add(s);
		east.add(e);
		west.add(w);

		f.add(north, BorderLayout.NORTH);
		f.add(south, BorderLayout.SOUTH);
		f.add(east, BorderLayout.EAST);
		f.add(west, BorderLayout.WEST);
	}

	public static void panelBorder(JPanel p){

		Border bc = BorderFactory.createEmptyBorder(10,10,10,10);
		Color myColor = Color.decode("#00ffff");
		Border br = BorderFactory.createLineBorder(myColor, 2);

		p.setLayout(new GridBagLayout());
		p.setBorder(br);
	}

	private void connection(){
		if(AllUtils.provaConnessioneThred().equals("CONNESSO"))
			connesso = true;
		else
			connesso = false;
	}



}
