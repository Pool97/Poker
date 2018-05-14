import client.socket.ClientManager;
import client.view.GameFrame;

public class GameTest {
    public static void main(String[] args) {
        new GameFrame(new ClientManager("192.168.1.6", 4040));
    }
}
