package client.socket;

import interfaces.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class SocketWriter<T extends Message> extends SwingWorker<Void, Void> {
    private ObjectOutputStream outputStream;
    private T message;

    public SocketWriter(ObjectOutputStream outputStream, T message) {
        this.outputStream = outputStream;
        this.message = message;
        System.out.println("Scritto il messaggio!");
    }

    @Override
    protected Void doInBackground() {
        try {
            System.out.println("Scritto il messaggio!");
            outputStream.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void process(List<Void> chunks) {
        super.process(chunks);
    }
}
