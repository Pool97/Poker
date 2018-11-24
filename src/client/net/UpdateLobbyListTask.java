package client.net;

import client.events.EventsAdapter;
import client.ui.frames.BoardFrame;
import client.ui.frames.Lobby;
import interfaces.ServerEvent;
import server.events.EventsContainer;
import server.events.PlayerLoggedEvent;
import server.events.RoomCreatedEvent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateLobbyListTask extends SwingWorker<Void, EventsContainer> {
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
        EventsContainer messageObject;
        do {
            Client.logger.info(WAITING);
            try {
                messageObject = (EventsContainer) Client.getInstance().readMessage();
                publish(messageObject);
                Thread.sleep(800);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!processor.isRoomCreated());
        return null;
    }

    @Override
    protected void process(List<EventsContainer> chunks) {
        EventsContainer eventsContainer = chunks.get(0);
        eventsContainer.getEvents().forEach(event -> ((ServerEvent) event).accept(processor));
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
    public void process(PlayerLoggedEvent event) {
        if (!playerLogged.contains(event.getNickname())) {
            playerLogged.add(event.getNickname());
            JLabel nickname = new JLabel(event.getNickname(), SwingConstants.CENTER);
            nickname.setAlignmentX(Component.CENTER_ALIGNMENT);
            nickname.setFont(new Font("helvetica", Font.BOLD, 20));
            nickname.setForeground(Color.WHITE);
            playerList.add(nickname);
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