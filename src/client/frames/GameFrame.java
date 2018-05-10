package client.frames;

import client.socket.ClientManager;
import client.socket.SocketWriter;
import events.Events;
import events.PlayerCreatedEvent;

/**
 * Frame di attesa per il Player.
 * Il Player si connette alla stanza e viene fornita una grafica che permette di monitorare gli attuali
 * Players in attesa all'interno della stanza e di mostrare il numero di players mancanti.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class GameFrame extends AbstractGameFrame {

    public GameFrame(String nickname, String avatar, String ipAddr) {
        this.nickname = nickname;
        clientManager = new ClientManager(ipAddr, 4040);
        clientManager.attemptToConnect();
        new SocketReaderStart<>(clientManager.getInputStream()).execute();

        SocketWriter socketWriter = new SocketWriter<>(clientManager.getOutputStream(),
                new Events(new PlayerCreatedEvent(nickname, avatar)));
        socketWriter.execute();

        initPanel();
        createGUI();
    }
}
