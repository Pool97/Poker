package client.net;

import client.events.EventsAdapter;
import client.ui.components.Avatar;
import client.ui.frames.BoardFrame;
import client.ui.frames.Lobby;
import interfaces.Event;
import interfaces.ServerEvent;
import server.events.PlayerDisconnected;
import server.events.PlayerLogged;
import server.events.RoomCreated;
import utils.Utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UpdateLobbyListTask extends SwingWorker<Void, Event> {
    private final static String WAITING = "In attesa della creazione della stanza...";
    private EventsProcessor processor;
    private Lobby frame;
    private String nickname;

    public UpdateLobbyListTask(Lobby frame, JPanel playerList, String nickname) {
        processor = new EventsProcessor(new ArrayList<>(), playerList );
        this.frame = frame;
        this.nickname = nickname;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Void doInBackground(){
        Event messageObject;
        do {
            Client.logger.info(WAITING);
                messageObject = ClientWrapper.getInstance().readMessage();
                publish(messageObject);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!processor.isRoomCreated());
        return null;
    }

    @Override
    protected void process(List<Event> chunks) {
        Event event = chunks.get(0);
        ((ServerEvent) event).accept(processor);
        frame.validate();
    }

    @Override
    protected void done() {
        frame.dispose();
        new BoardFrame(nickname);
    }
}

class EventsProcessor extends EventsAdapter {
    private boolean isRoomCreated = false;
    private ArrayList<String> playerLogged;
    private JPanel playerList;
    private HashMap<String, JPanel> nicknameTuples;

    public EventsProcessor(ArrayList<String> playerLogged, JPanel playerList){
        this.playerLogged = playerLogged;
        this.playerList = playerList;
        nicknameTuples = new HashMap<>();

    }

    @Override
    public void process(PlayerLogged event) {
        if (!playerLogged.contains(event.getNickname())) {
            playerLogged.add(event.getNickname());

            JPanel tupleA = new JPanel();
            SpringLayout layoutA = new SpringLayout();
            tupleA.setLayout(layoutA);
            tupleA.setBackground(Utils.TRANSPARENT);
            tupleA.setOpaque(false);
            //System.out.println("Size: " + tupleA.getWidth() + " " + tupleA.getHeight());
            Avatar avatar = new Avatar(event.getAvatar());
            avatar.setMinimumSize(true);
            avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
            tupleA.add(avatar);
            layoutA.putConstraint(SpringLayout.EAST, avatar,-10, SpringLayout.EAST, tupleA);
            layoutA.putConstraint(SpringLayout.VERTICAL_CENTER, avatar,0, SpringLayout.VERTICAL_CENTER, tupleA);

            JPanel tupleN = new JPanel();
            SpringLayout layoutN = new SpringLayout();
            tupleN.setLayout(layoutN);
            tupleN.setBackground(Utils.TRANSPARENT);
            tupleA.setOpaque(false);
            JLabel nickname = new JLabel(event.getNickname(), SwingConstants.CENTER);
            nickname.setFont(new Font("helvetica", Font.BOLD, 25));
            nickname.setForeground(Color.WHITE);
            nickname.setVerticalAlignment(SwingConstants.CENTER);
            tupleN.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            tupleN.add(nickname);
            layoutN.putConstraint(SpringLayout.VERTICAL_CENTER, nickname,0, SpringLayout.VERTICAL_CENTER, tupleN);
            layoutN.putConstraint(SpringLayout.WEST, nickname,10, SpringLayout.WEST, tupleN);
            //tuple.add(Box.createHorizontalGlue());
            JPanel tuple = new JPanel();
            Border redBorder;
            redBorder = BorderFactory.createLineBorder(new Color(0, 117, 178) ,2);
            tuple.setBorder(redBorder);
            tuple.setLayout(new GridLayout(0,2));
            tuple.setBackground(Utils.TRANSPARENT);
            tuple.setOpaque(false);
            tuple.add(tupleA);
            tuple.add(tupleN);
            nicknameTuples.put(event.getNickname(), tuple);
            playerList.add(tuple);
        }
    }

    @Override
    public void process(PlayerDisconnected event) {
        playerList.remove(nicknameTuples.get(event.getNickname()));
        playerList.revalidate();
        playerList.repaint();
    }

    @Override
    public void process(RoomCreated event) {
        isRoomCreated = true;
    }

    public boolean isRoomCreated() {
        return isRoomCreated;
    }
}

