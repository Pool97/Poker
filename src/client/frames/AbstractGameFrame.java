package client.frames;


import client.events.EventsAdapter;
import client.socket.ClientManager;
import interfaces.ServerEvent;
import server.events.Events;
import server.events.PlayerLoggedEvent;
import server.events.RoomCreatedEvent;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Permette la creazione di un frame di attesa per il PlayerBoard che si connette alla stanza. È una classe "astratta",
 * nel senso che rappresenta un buon modello di Frame da cui partire per specializzare quelli del creatore della
 * stanza e quelli degli altri players, che hanno comportamenti diversi in fase di avvio di questo frame.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class AbstractGameFrame extends JFrame {
    private final static String FRAME_TITLE = "In attesa degli altri players...";
    private final static String WAITING_FOR_PLAYERS = " Rimani in attesa degli altri players.";
    private final static String OK = "Ok, ";

    protected String nickname;
    protected String ipAddress;
    protected ClientManager clientManager;
    private JPanel playersList;
    private ArrayList<String> nicknames;

    public AbstractGameFrame(String ipAddress) {
        nicknames = new ArrayList<>();
        this.ipAddress = ipAddress;
    }

    protected void initPanel() {

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBackground(new Color(0, 117, 178));
        playersList = new JPanel();
        playersList.setBackground(new Color(2, 168, 255));
        playersList.setAlignmentX(Component.CENTER_ALIGNMENT);
        playersList.add(Box.createVerticalStrut(10));
        playersList.setLayout(new BoxLayout(playersList, BoxLayout.Y_AXIS));

        JLabel hostInfo = new JLabel("Server IP: " + ipAddress, SwingConstants.CENTER);
        hostInfo.setFont(new Font("helvetica", Font.BOLD, 20));
        hostInfo.setForeground(Color.WHITE);
        hostInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel playerConnected = new JLabel("Giocatori connessi", SwingConstants.CENTER);
        playerConnected.setForeground(Color.WHITE);
        playerConnected.setFont(new Font("helvetica", Font.BOLD, 30));
        container.add(Box.createVerticalStrut(20));
        container.add(playerConnected, BorderLayout.NORTH);
        container.add(Box.createVerticalStrut(20));
        container.add(hostInfo, BorderLayout.SOUTH);
        container.add(playersList, BorderLayout.CENTER);
        add(container);
    }

    protected void createGUI() {
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(FRAME_TITLE);
        setVisible(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 400);
    }

    /**
     * Gestore degli eventi ricevuti da Server. È incaricato di aggiornare opportunamente la grafica, in base
     * al tipo di messaggio ricevuto.
     */

    class EventsProcessor extends EventsAdapter {
        private boolean isRoomCreated = false;

        @Override
        public void process(PlayerLoggedEvent event) {
            if (!nicknames.contains(event.getNickname())) {
                nicknames.add(event.getNickname());
                JLabel nickname = new JLabel(event.getNickname(), SwingConstants.CENTER);
                nickname.setAlignmentX(Component.CENTER_ALIGNMENT);
                nickname.setFont(new Font("helvetica", Font.BOLD, 20));
                nickname.setForeground(Color.WHITE);
                playersList.add(nickname);
                validate();
            }

        }

        @Override
        public void process(RoomCreatedEvent event) {
            isRoomCreated = true;
        }

        public boolean isRoomCreated() {
            return isRoomCreated;
        }
    }

    public class SocketReaderStart extends SwingWorker<Void, Events> {
        private final static String WAITING = "In attesa della creazione della stanza...";
        private ObjectInputStream inputStream;
        private EventsProcessor processor;

        /**
         * Costruttore di SocketReaderStart.
         * @param inputStream L'ObjectInputStream del Socket che garantisce la connessione con il Server.
         */

        public SocketReaderStart(ObjectInputStream inputStream) {
            this.inputStream = inputStream;
            processor = new EventsProcessor();
        }

        /**
         * Permette di rimanere in attesa dei messaggi inviati dal Server su un Thread diverso dall'EDT, in accordo
         * con la Single-Thread Rule.
         */

        @SuppressWarnings("unchecked")
        @Override
        protected Void doInBackground(){
            Events messageObject;
            do {
                ClientManager.logger.info(WAITING);
                try {
                    messageObject = (Events) inputStream.readObject();
                    publish(messageObject);
                    Thread.sleep(800);

                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!processor.isRoomCreated());
            return null;
        }

        /**
         * Questo metodo viene richiamato dallo SwingWorker ed eseguito direttamente sull'EDT. Si occupa di informare
         * il gestore degli eventi {@link EventsProcessor} di aggiornare opportunamente la grafica, in base al tipo
         * di messaggio ricevuto da Server.
         *
         * @param chunks Messaggio ricevuto dal Server
         */

        @Override
        protected void process(List<Events> chunks) {
            Events events = chunks.get(0);
            events.getEvents().forEach(event -> ((ServerEvent) event).accept(processor));
        }

        /**
         * Questo metodo viene richiamato nel momento in cui lo SwingWorker riceve il messaggio di avvenuta creazione
         * della stanza ({@link RoomCreatedEvent}). Esso sancisce l'uscita dal Thread dello SwingWorker creando
         * la schermata principale del gioco.
         */

        @Override
        protected void done() {
            dispose();
            new BoardFrame(clientManager, nickname);
        }
    }
}
