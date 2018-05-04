package client.frames;


import client.socket.ClientManager;
import client.socket.SocketWriter;
import client.view.Game;
import events.*;
import interfaces.Message;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;


public class GameFrame extends JFrame {
    private final static String FRAME_TITLE = "In attesa degli altri players...";
    private final static String WAITING_FOR_PLAYERS = " Rimani in attesa degli altri players.";
    private final static String OK = "Ok, ";
    private String nickname;
    private ClientManager clientManager;
    private SocketReaderStart<? extends Message> reader;

    public GameFrame(String nickname, String avatar) {
        this.nickname = nickname;
        clientManager = new ClientManager("localhost", 4040);
        clientManager.attemptToConnect();
        reader = new SocketReaderStart<>(clientManager.getInputStream());
        reader.execute();

        Events events = new Events();
        events.addEvent(new PlayerCreatedEvent(nickname, avatar));
        events.addEvent(new CreatorConnectedEvent(1));
        SocketWriter socketWriter = new SocketWriter<>(clientManager.getOutputStream(), events);
        socketWriter.execute();
        frameSetUp(FRAME_TITLE);
        game();

    }

    private void game() {
        JPanel panelG = new JPanel();
        panelG.setLayout(new GridBagLayout());
        //Main.panelBorder(panelG);

        GridBagConstraints cC = new GridBagConstraints();

        cC.insets = new Insets(2, 2, 2, 2); // insets for all components
        cC.gridx = 0; // column 0
        cC.gridy = 0; // row 0
        cC.ipadx = 5; // increases components width by 10 pixels
        cC.ipady = 5; // increases components height by 10 pixels
        JLabel lbl = new JLabel("<html>" + "<center>" + OK + nickname + "</center>" + ". <br>" + WAITING_FOR_PLAYERS + "<html>");
        Font font = new Font("Courier", Font.BOLD, 20);
        lbl.setFont(font);
        lbl.setForeground(Color.GRAY);
        panelG.add(lbl, cC);

        cC.gridx = 0; // column 0
        cC.gridy = 1; // row 0
        cC.gridx = 0; // column 0
        cC.gridy = 3; // row 0
        add(panelG, BorderLayout.CENTER);
    }

    private void frameSetUp(String str) {
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));
        setTitle(str);
        setVisible(true);

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

        add(north, BorderLayout.NORTH);
        add(south, BorderLayout.SOUTH);
        add(east, BorderLayout.EAST);
        add(west, BorderLayout.WEST);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 400);
    }

    class EventProcessor extends EventProcessAdapter {
        private boolean isRoomCreated = false;

        @Override
        public void process(PlayerAddedEvent event) {
            System.out.println("To do!");
        }

        @Override
        public void process(RoomCreatedEvent event) {
            isRoomCreated = true;
        }

        public boolean isRoomCreated() {
            return isRoomCreated;
        }
    }

    public class SocketReaderStart<T extends Message> extends SwingWorker<Void, T> {
        private final static String WAITING = "In attesa di un messaggio dal Server...";
        private ObjectInputStream inputStream;
        private EventProcessor processor;

        public SocketReaderStart(ObjectInputStream inputStream) {
            this.inputStream = inputStream;
            processor = new EventProcessor();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Void doInBackground() throws IOException, ClassNotFoundException {
            T messageObject;
            //ClientManager.logger.info(WAITING);
            do {
                messageObject = (T) inputStream.readObject();
                if (messageObject instanceof Events) {
                    Events events = (Events) messageObject;
                    events.getEvents().forEach(event -> event.accept(processor));
                    if (processor.isRoomCreated())
                        return null;
                }
            } while (true);
        }

        @Override
        protected void done() {
            new Game(clientManager);
            dispose();
        }
    }
}
