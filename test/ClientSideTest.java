import client.ClientSocketManager;
import client.messages.CreationRoomMessage;
import client.messages.WelcomePlayerMessage;

public class ClientSideTest {

    public static void main(String [] args){
        CreationRoomMessage creatorMessage = new CreationRoomMessage( 3, 20000);
        WelcomePlayerMessage creator = new WelcomePlayerMessage("Pool97", "creator.png");
        WelcomePlayerMessage perryMessage = new WelcomePlayerMessage("Perry97", "perry.png");
        WelcomePlayerMessage tunsiMessage = new WelcomePlayerMessage("Tunsi97", "tunsi.png");

        ClientSocketManager creatorClient = new ClientSocketManager("localhost", 4040);
        creatorClient.sendMessage(creatorMessage);
        creatorClient.sendMessage(creator);

        ClientSocketManager perryClient = new ClientSocketManager("localhost", 4040);
        perryClient.sendMessage(perryMessage);

        ClientSocketManager tunsiClient = new ClientSocketManager("localhost", 4040);
        tunsiClient.sendMessage(tunsiMessage);
    }
}
