package client.frames;

import org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel;
import utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * È la schermata iniziale del gioco. Vengono proposte le seguenti opzioni all'utente:
 * <p>
 * - Crea una stanza: permette di creare una stanza nella rete locale in cui risiedono i giocatori;
 * - Connettiti: permette di connettersi a una determinata stanza;
 * - About us: scopri qualcosa su di noi!
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class WelcomeFrame extends JFrame {
    private final static String FRAME_TITLE = "Benvenuto in Poker Texas!";
    private final static String ROOM_CREATION_OPTION = "Crea una stanza";
    private final static String ROOM_CONNECT_OPTION = "Connettiti";
    private final static String ABOUT_US_OPTION = "About us";
    private final static String ROOM_CREATION_INFO = "La stanza verrà hostata sulla porta 4040. Invia questi dati agli altri utenti.";

    private final static String MESSAGE_DIALOG_ROOM = "Creazione della stanza";

    /**
     * Costruttore vuoto.
     * Invoca i metodi necessari per generare l'interfaccia grafica.
     */

    public WelcomeFrame() {
        setLookAndFeel();
        createGUI();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            try {
                WelcomeFrame frame = new WelcomeFrame();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle(FRAME_TITLE);
                frame.setVisible(true);
                frame.setResizable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Permette di creare l'interfaccia grafica per l'utente.
     */

    private void createGUI() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        container.add(Box.createHorizontalGlue());

        JButton roomButton = new JButton();
        setCustomButton(roomButton, ROOM_CREATION_OPTION, Color.GREEN);
        container.add(roomButton);
        container.add(Box.createHorizontalGlue());
        roomButton.addActionListener(event -> {
            int userResponse = JOptionPane.showOptionDialog(this, ROOM_CREATION_INFO, MESSAGE_DIALOG_ROOM,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            if (userResponse == JOptionPane.OK_OPTION) {
                dispose();
                new AvatarFrame(0);
            }
        });

        JButton connectButton = new JButton();
        setCustomButton(connectButton, ROOM_CONNECT_OPTION, Color.BLUE);
        container.add(connectButton);
        container.add(Box.createHorizontalGlue());
        connectButton.addActionListener(event -> {
            dispose();
            new AvatarFrame(1);
        });
        JButton infoButton = new JButton();
        setCustomButton(infoButton, ABOUT_US_OPTION, Color.CYAN);
        container.add(infoButton);
        container.add(Box.createHorizontalGlue());

        add(container);
        pack();
    }

    private void setCustomButton(JButton button, String text, Color color) {
        button.setFont(Utils.getCustomFont(Font.PLAIN, 18));
        button.setText(text);
        button.setFocusPainted(false);
        button.setAlignmentX(CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(200, 200));
        button.setMaximumSize(new Dimension(200, 200));
        button.setBackground(color);
    }

    /**
     * Permette di impostare il tema del programma.
     */

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new SubstanceMistAquaLookAndFeel());
        } catch (UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 500);
    }
}
