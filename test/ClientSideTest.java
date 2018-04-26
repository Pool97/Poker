import client.socket.ClientManager;
import events.*;

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
        Events creatorEvents = new Events();
        creatorEvents.addEvent(new PlayerCreatedEvent("Pool97", "creator.png"));
        creatorEvents.addEvent(new TotalPlayersEvent(1));
        ClientManager creatorClient = new ClientManager("localhost", 4040);
        creatorClient.attemptToConnect();
        creatorClient.sendMessage(creatorEvents);
        Events playersList = creatorClient.listenForAMessage();

        Events startTurn = creatorClient.listenForAMessage();

        while (!playersList.isEmpty()) {
            PlayerAddedEvent playerEvent = (PlayerAddedEvent) playersList.getEvent();
            System.out.println(playerEvent.getNickname() + " " + playerEvent.getPosition());
        }

        SmallBlindEvent smallBlindEvent = (SmallBlindEvent) startTurn.getEvent();
        BigBlindEvent bigBlindEvent = (BigBlindEvent) startTurn.getEvent();
        System.out.println(smallBlindEvent.getSmallBlind() + " " + bigBlindEvent.getBigBlind());
    }
}
