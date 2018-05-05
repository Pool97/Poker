package client.frames;


import client.socket.ClientManager;
import client.view.Game;
import events.Events;
import events.PlayerAddedEvent;
import events.RoomCreatedEvent;
import interfaces.Message;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;


public class AbstractGameFrame extends JFrame {
    private final static String FRAME_TITLE = "In attesa degli altri players...";
    private final static String WAITING_FOR_PLAYERS = " Rimani in attesa degli altri players.";
    private final static String OK = "Ok, ";

    protected String nickname;
    protected ClientManager clientManager;
    protected SocketReaderStart<? extends Message> reader;
    private JPanel playersList;

    public AbstractGameFrame() {

    }

    protected void game() {
        JPanel panelGraphic = new JPanel();
        playersList = new JPanel();
        JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelGraphic, playersList);
        add(splitter, BorderLayout.CENTER);
    }

    protected void frameSetUp() {
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

    class EventProcessor extends EventProcessAdapter {
        private boolean isRoomCreated = false;

        @Override
        public void process(PlayerAddedEvent event) {
            playersList.add(new JLabel(event.getNickname()));
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
