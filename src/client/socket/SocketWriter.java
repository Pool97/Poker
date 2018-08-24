package client.socket;

import server.events.Events;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class SocketWriter extends SwingWorker<Void, Void> {
    private ObjectOutputStream outputStream;
    private Events message;

    private final static String WRITE_ERROR = "Errore nell'invio del messaggio al Server";

    public SocketWriter(ObjectOutputStream outputStream, Events message) {
        this.outputStream = outputStream;
        this.message = message;
    }

    @Override
    protected Void doInBackground() {
        try {
            outputStream.writeObject(message);
            outputStream.flush();

        } catch (IOException e) {
            ClientManager.logger.finer(WRITE_ERROR);
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return null;
    }

    @Override
    protected void process(List<Void> chunks) {
        super.process(chunks);
    }
}
