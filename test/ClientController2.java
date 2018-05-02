import client.socket.ClientManager;
import client.socket.SocketReader;
import client.socket.SocketWriter;
import events.Events;
import events.PlayerCreatedEvent;

import javax.swing.*;

public class ClientController2 {
    public static void main(String... args) {
        Events clientEvents = new Events();
        clientEvents.addEvent(new PlayerCreatedEvent("Perry97", "perry.png"));

        ClientManager clientManager = new ClientManager("localhost", 4040);
        clientManager.attemptToConnect();
        SocketWriter socketWriter = new SocketWriter<>(clientManager.getOutputStream(), clientEvents);
        socketWriter.execute();
        SocketReader socketReader = new SocketReader(clientManager.getInputStream());
        socketReader.execute();
        JOptionPane.showConfirmDialog(null, "Cancel", "Cancel this task?", JOptionPane.DEFAULT_OPTION);
    }
}
