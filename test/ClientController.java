import client.socket.ClientManager;
import client.socket.SocketReader;
import client.socket.SocketWriter;
import events.CreatorConnectedEvent;
import events.Events;
import events.PlayerCreatedEvent;

import javax.swing.*;

public class ClientController {

    public static void main(String... args) {
        Events creatorEvents = new Events();
        creatorEvents.addEvent(new PlayerCreatedEvent("Pool97", "creator.png"));
        creatorEvents.addEvent(new CreatorConnectedEvent(2));

        ClientManager clientManager = new ClientManager("localhost", 4040);
        clientManager.attemptToConnect();
        SocketWriter socketWriter = new SocketWriter<>(clientManager.getOutputStream(), creatorEvents);
        socketWriter.execute();
        SocketReader socketReader = new SocketReader(clientManager.getInputStream());
        socketReader.execute();
        JOptionPane.showConfirmDialog(null, "Cancel", "Cancel this task?", JOptionPane.DEFAULT_OPTION);
    }
}