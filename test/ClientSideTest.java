import client.messages.CreationRoomMessage;
import client.messages.WelcomeMessage;
import client.socket.ClientManager;
import server.messages.PlayersMessage;
import server.messages.StartMessage;

public class ClientSideTest {

    /**
     * Prima di eseguire questo Test, assicurarsi che si abbia un PC prestante.
     * Con un PC normale si può eseguire questo test senza ritoccare nulla nei commenti,
     * se si ha un PC prestante provare a decommentare un client alla volta per eseguire
     * test più approfonditi sul funzionamento dell'infrastruttura.
     *
     * @param args
     */
    public static void main(String[] args) {
        CreationRoomMessage creatorMessage = new CreationRoomMessage(1);
        WelcomeMessage creator = new WelcomeMessage("Pool97", "creator.png");
        //WelcomeMessage perryMessage = new WelcomeMessage("Perry97", "perry.png");
        //WelcomeMessage tunsiMessage = new WelcomeMessage("Tunsi97", "tunsi.png");

        ClientManager creatorClient = new ClientManager("localhost", 4040);
        creatorClient.attemptToConnect();
        creatorClient.sendMessage(creatorMessage);
        creatorClient.sendMessage(creator);

        //ClientManager perryClient = new ClientManager("localhost", 4040);
        //perryClient.sendMessage(perryMessage);

        //ClientManager tunsiClient = new ClientManager("localhost", 4040);
        //tunsiClient.sendMessage(tunsiMessage);


        PlayersMessage messageRob = creatorClient.listenForAMessage();
        //PlayersMessage messagePerry = perryClient.listenForAMessage();

        StartMessage startRob = creatorClient.listenForAMessage();
        //StartMessage startPerry = perryClient.listenForAMessage();
        //PlayersMessage messageTunsi = tunsiClient.listenForAMessage();
        System.out.println(messageRob.getPlayersInfo().size());
        //System.out.println(messagePerry.getPlayersInfo().size());
        System.out.println(startRob.getBigBlind());
        //System.out.println(startPerry.getSmallBlind());
        //System.out.println(messageTunsi.getPlayersInfo().size());
        System.out.println("ciaone");
    }
}
