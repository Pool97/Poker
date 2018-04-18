package view;

import server.socket.ClientSocketManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Hashtable;

import static server.socket.MainScreen.panelBorder;
import static view.AvatarFrame.txtInput;
import static view.SecondFrame.sFrame;

public class ClientFrame {

    private JFrame client;
    private JTextField txtInputCl;
    private JTextField test;
    public static JTextArea txtLogCl;
    private JSlider slider;
    public static JButton bet;

    public void cl() throws IOException {

        try {
            client = new JFrame();
            client.setBounds(100, 100, 450, 300);
            client.setTitle("CLIENT " + txtInput.getText());
            client.setVisible(true);

            JScrollPane scrollPane = new JScrollPane();
            client.add(scrollPane, BorderLayout.CENTER);

            txtLogCl = new JTextArea();
            scrollPane.setViewportView(txtLogCl);

            /*JButton btnBtn = new JButton();
            client.add(btnBtn, BorderLayout.SOUTH);*/

            JPanel panel= new JPanel();
            panelBorder(panel);

            JPanel panel2= new JPanel();
            panelBorder(panel2);

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(2, 2, 2, 2); // insets for all components
            c.gridx = 0; // column 0
            c.gridy = 0; // row 0
            c.ipadx = 5; // increases components width by 10 pixels
            c.ipady = 5; // increases components height by 10 pixels
            JLabel clientText = new JLabel();
            clientText.setText("Inserirsci il testo da inviare al server:");
            panel.add(clientText, c);

            c.gridx = 1; // column 1
            c.gridy = 0; // row 0
            // c.gridy = 0; // comment out this for reusing the obj
            txtInputCl = new JTextField();
            txtInputCl.setColumns(10);
            panel.add(txtInputCl, c);
            c.gridx = 2; // column 2
            c.gridy = 0; // row 0
            JButton btnAdd = new JButton("Invia");
            panel.add(btnAdd, c);
            client.add(panel, BorderLayout.NORTH);

            GridBagConstraints c2 = new GridBagConstraints();
            c2.insets = new Insets(2, 2, 2, 2); // insets for all components

            c2.gridx = 0;
            c2.gridy = 0;
            c2.anchor = GridBagConstraints.LINE_END;
            test = new JTextField(JTextField.SOUTH);
            panel2.add(test,c2);

            c2.gridx = 5;
            c2.gridy = 0;
            c2.anchor = GridBagConstraints.LINE_START;
            bet = new JButton("BET");
            panel2.add(bet,c2);

            c2.gridx = 1; // column 1
            c2.gridy = 0; // row 1
            c2.ipady = 5;
            c2.weightx = 0.0;
            c2.gridwidth = 3;
            c2.anchor = GridBagConstraints.LINE_END;
            // Set the slider
            slider = new JSlider();
            slider.setMaximum(10000);
            slider.setMinorTickSpacing(1000);
            slider.setMajorTickSpacing(5000);
            slider.setPaintTicks(true);
            // Set the labels to be painted on the slider
            slider.setPaintLabels(true);

            Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
            position.put(0, new JLabel("0"));
            position.put(10000, new JLabel("10000"));

            // Set the label to be drawn
            slider.setLabelTable(position);

            test.setText(String.valueOf(slider.getValue()));

            test.addKeyListener(new KeyAdapter(){
                @Override
                public void keyReleased(KeyEvent ke) {
                    if(!test.getText().equals("")){
                        raise();
                        minore();
                        String typed = test.getText();
                        slider.setValue(0);
                        if(!typed.matches("\\d+(\\.\\d*)?")) {
                            return;
                        }
                        int value = Integer.parseInt(typed);
                        slider.setValue(value);
                    }
                }
            });

            final DecimalFormat db = new DecimalFormat();
            // Add change listener to the slider
            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    int sValue = ((JSlider)e.getSource()).getValue();
                    //status.setText("Slider value: "+ sValue);
                    test.setText(db.format(slider.getValue()));

                }});

            bet.addActionListener(e -> {
                updateLogNum(test.getText());
                test.setText("");
            });


            // Add the slider to the panel
            panel2.add(slider,c2);
            client.add(panel2, BorderLayout.SOUTH);

            new Thread(new ClientSocketManager(txtInput.getText())).start();

            btnAdd.addActionListener(e -> {
                updateLogTxt(txtInputCl.getText());
                txtInputCl.setText("");
            });

            //new Thread(new TCPClient()).start();
        }catch (NullPointerException e){
            String msg = "<html><font color=\"#cc0000\">Allora sei proprio Coglione, te l'avevo pure detto di creare l'Avatar prima!</font></html>";
            JOptionPane.showMessageDialog(sFrame, msg);
        }


    }

    private void updateLogTxt(String text) {
        txtLogCl.append(String.format("%4d - %s\n", txtLogCl.getLineCount(), text));
        ClientSocketManager.client_msg = text;
        txtInputCl.selectAll();
    }
    
    private void updateLogNum(String numero) {
        txtLogCl.append(String.format("%4d - %s\n", txtLogCl.getLineCount(), numero));
        ClientSocketManager.client_msg = numero;
        ClientSocketManager.btn_status = bet.getText();
        test.selectAll();
    }

    private void raise(){
        int v = Integer.parseInt(test.getText());
        if(v > slider.getMinimum()){
            slider.setMinimum(v);
            test.setText(String.valueOf(v));
            bet.setText("RAISE");
        }
    }

    private void minore(){
        int v = Integer.parseInt(test.getText());
        if(v < slider.getMinimum()){
            bet.setEnabled(false);
        }else
            bet.setEnabled(true);
    }
}
