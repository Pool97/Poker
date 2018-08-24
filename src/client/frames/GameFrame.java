package client.frames;

import client.events.PlayerConnectedEvent;
import client.socket.ClientManager;
import client.socket.SocketWriter;
import server.events.Events;

/**
 * Frame di attesa per il PlayerBoard.
 * Il PlayerBoard si connette alla stanza e viene fornita una grafica che permette di monitorare gli attuali
 * Players in attesa all'interno della stanza e di mostrare il numero di players mancanti.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class GameFrame extends AbstractGameFrame {

    public GameFrame(String nickname, String avatar, String ipAddr) {
        super(ipAddr);
        this.nickname = nickname;
        clientManager = new ClientManager(ipAddr, 4040);
        clientManager.attemptToConnect();
        new SocketReaderStart(clientManager.getInputStream()).execute();

        SocketWriter socketWriter = new SocketWriter(clientManager.getOutputStream(),
                new Events(new PlayerConnectedEvent(nickname, avatar)));
        socketWriter.execute();

        initPanel();
        createGUI();
    }
}
