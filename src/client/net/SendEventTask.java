package client.net;

import interfaces.Event;

import javax.swing.*;

public class SendEventTask extends SwingWorker<Void, Void> {
    private Event message;

    public SendEventTask(Event message) {
        this.message = message;
    }

    @Override
    protected Void doInBackground() {
        ClientWrapper.getInstance().writeMessage(message);
        return null;
    }
}
