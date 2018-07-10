package client.frames;

import client.events.CreatorConnectedEvent;
import client.events.PlayerConnectedEvent;
import client.socket.ClientManager;
import client.socket.SocketWriter;
import server.ServerThread;
import server.events.Events;

/**
 * Frame di attesa per il creatore della stanza.
 * Permette di creare la stanza, istanziando il Server, e viene fornita una grafica che permette di monitorare gli
 * attuali Players in attesa all'interno della stanza e di mostrare il numero di players mancanti.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class CreatorGameFrame extends AbstractGameFrame {

    public CreatorGameFrame(String nickname, String avatar, int totalPlayers, String ipAddr) {
        this.nickname = nickname;

        new Thread(new ServerThread()).start();
        clientManager = new ClientManager(ipAddr, 4040);
        clientManager.attemptToConnect();
        new SocketReaderStart<>(clientManager.getInputStream()).execute();
        Events events = new Events(new PlayerConnectedEvent(nickname, avatar),
                new CreatorConnectedEvent(totalPlayers));
        new SocketWriter<>(clientManager.getOutputStream(), events).execute();

        initPanel();
        createGUI();
    }

}
