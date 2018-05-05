package client.frames;

import client.socket.ClientManager;
import client.socket.SocketWriter;
import events.CreatorConnectedEvent;
import events.Events;
import events.PlayerCreatedEvent;
import server.ServerThread;

public class CreatorGameFrame extends AbstractGameFrame {

    public CreatorGameFrame(String nickname, String avatar, int totalPlayers) {
        new Thread(new ServerThread()).start();

        this.nickname = nickname;
        clientManager = new ClientManager("localhost", 4040);
        clientManager.attemptToConnect();
        reader = new SocketReaderStart<>(clientManager.getInputStream());
        reader.execute();

        Events events = new Events();
        events.addEvent(new PlayerCreatedEvent(nickname, avatar));
        events.addEvent(new CreatorConnectedEvent(totalPlayers));
        SocketWriter socketWriter = new SocketWriter<>(clientManager.getOutputStream(), events);
        socketWriter.execute();
        game();
        frameSetUp();
    }

}
