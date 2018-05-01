package client.socket;

import interfaces.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SocketWriter<T extends Message> extends SwingWorker<Void, Void> {
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private T message;

    public SocketWriter(OutputStream outputStream, T message) {
        this.outputStream = outputStream;
        this.message = message;
    }

    @Override
    protected Void doInBackground() {
        createStreams();

        return null;
    }

    private void createStreams() {
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FUCK");
            Thread.currentThread().interrupt();
        }
    }
}
