package client.socket;

import interfaces.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class SocketWriter<T extends Message> extends SwingWorker<Void, Void> {
    private ObjectOutputStream outputStream;
    private T message;

    private final static String WRITE_ERROR = "Errore nell'invio del messaggio al Server";

    public SocketWriter(ObjectOutputStream outputStream, T message) {
        this.outputStream = outputStream;
        this.message = message;
    }

    @Override
    protected Void doInBackground() {
        try {
            outputStream.reset();
            outputStream.writeObject(message);

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
