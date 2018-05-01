package client.socket;

import interfaces.Message;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class SocketReader<T extends Message> extends SwingWorker<Void, T> {
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    public SocketReader(InputStream stream) {
        this.inputStream = stream;
    }

    @Override
    protected Void doInBackground() throws IOException, ClassNotFoundException {
        createStreams();
        T messageObject = null;
        do {
            messageObject = (T) objectInputStream.readObject();
            System.out.println("Guarda, ho letto un nuovo messaggio!");
            publish(messageObject);
        } while (true);
    }

    @Override
    protected void process(List<T> chunks) {
        super.process(chunks);
    }

    private void createStreams() {
        try {
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FUCK");
            Thread.currentThread().interrupt();
        }
    }
}
