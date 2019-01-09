package client.net;

import client.ui.frames.Lobby;
import interfaces.Event;
import server.events.LoggingStatus;

import javax.swing.*;
import java.util.List;

public class LoggingTask extends SwingWorker<Void, Event> {
    private boolean creator;
    private String nickname;
    private JDialog pokerDialog;


    public LoggingTask(JDialog pokerDialog, boolean creator, String nickname) {
        this.pokerDialog = pokerDialog;
        this.creator = creator;
        this.nickname = nickname;
    }

    @Override
    protected Void doInBackground() {
        Event event = ClientWrapper.getInstance().readMessage();
        publish(event);
        return null;
    }

    @Override
    protected void process(List<Event> chunks) {
        Event event = chunks.get(0);
        if (chunks.get(0) instanceof LoggingStatus) {
            LoggingStatus status = (LoggingStatus) event;
            if (status.isPermitted()) {
                openLobby();
            } else {
                JOptionPane.showMessageDialog(null, "è in corso una partita. Attendi per favore che termini prima di riconnetterti!");
            }
        }
    }

    private void openLobby() {
        new Lobby(nickname, creator);
        pokerDialog.dispose();
    }
}
