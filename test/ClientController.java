import client.socket.ClientManager;
import client.socket.SocketReader;

import java.util.concurrent.CountDownLatch;

public class ClientController {

    public static void main(String... args) throws InterruptedException {
        ClientManager clientManager = new ClientManager("localhost", 4040);
        clientManager.attemptToConnect();
        SocketReader socketReader = new SocketReader(clientManager.getInputStream());
        socketReader.execute();
        CountDownLatch count = new CountDownLatch(1);
        count.await();
    }
}
