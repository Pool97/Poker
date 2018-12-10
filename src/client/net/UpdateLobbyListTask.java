package client.net;

import client.events.EventsAdapter;
import client.ui.components.Avatar;
import client.ui.frames.BoardFrame;
import client.ui.frames.Lobby;
import interfaces.Event;
import interfaces.ServerEvent;
import server.events.PlayerLogged;
import server.events.RoomCreated;
import utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateLobbyListTask extends SwingWorker<Void, Event> {
    private final static String WAITING = "In attesa della creazione della stanza...";
    private EventsProcessor processor;
    private Lobby frame;

    public UpdateLobbyListTask(Lobby frame, JPanel playerList) {
        processor = new EventsProcessor(new ArrayList<>(), playerList);
        this.frame = frame;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Void doInBackground(){
        Event messageObject;
        do {
            Client.logger.info(WAITING);
            try {
                messageObject = (Event)Client.getInstance().readMessage();
                publish(messageObject);
                Thread.sleep(500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!processor.isRoomCreated());
        return null;
    }

    @Override
    protected void process(List<Event> chunks) {
        Event eventsContainer = chunks.get(0);
        ((ServerEvent)eventsContainer).accept(processor);
        frame.validate();
    }

    @Override
    protected void done() {
        frame.dispose();
        new BoardFrame();
    }
}

class EventsProcessor extends EventsAdapter {
    private boolean isRoomCreated = false;
    private ArrayList<String> playerLogged;
    private JPanel playerList;

    public EventsProcessor(ArrayList<String> playerLogged, JPanel playerList){
        this.playerLogged = playerLogged;
        this.playerList = playerList;
    }

    @Override
    public void process(PlayerLogged event) {
        if (!playerLogged.contains(event.getNickname())) {
            playerLogged.add(event.getNickname());
            JPanel tuple = new JPanel();
            tuple.add(Box.createHorizontalGlue());
            tuple.setLayout(new BoxLayout(tuple, BoxLayout.X_AXIS));
            tuple.setBackground(Utils.TRANSPARENT);
            tuple.setOpaque(false);
            Avatar avatar = new Avatar(event.getAvatar());
            avatar.setMinimumSize(true);
            avatar.setAlignmentX(Component.LEFT_ALIGNMENT);
            tuple.add(avatar);
            JLabel nickname = new JLabel(event.getNickname(), SwingConstants.CENTER);
            nickname.setFont(new Font("helvetica", Font.BOLD, 25));
            nickname.setForeground(Color.WHITE);
            tuple.add(nickname);
            tuple.add(Box.createHorizontalGlue());
            playerList.add(tuple);
        }
    }

    @Override
    public void process(RoomCreated event) {
        isRoomCreated = true;
    }

    public boolean isRoomCreated() {
        return isRoomCreated;
    }
}
