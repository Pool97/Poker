import client.socket.ClientManager;
import events.*;

public class ClientCreatorTest {

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
        creatorEvents.addEvent(new CreatorConnectedEvent(2));
        ClientManager creatorClient = new ClientManager("localhost", 4040);
        creatorClient.attemptToConnect();
        creatorClient.sendMessage(creatorEvents);
        Events playersList = creatorClient.listenForAMessage();

        Events startTurn = creatorClient.listenForAMessage();

        System.out.println("I giocatori attuali della partita sono: ");
        while (!playersList.isEmpty()) {
            PlayerAddedEvent playerEvent = (PlayerAddedEvent) playersList.getEvent();
            System.out.println(playerEvent.getNickname() + " " + playerEvent.getPosition());
        }


        BlindsUpdatedEvent blindsUpdatedEvent = (BlindsUpdatedEvent) startTurn.getEvent();
        System.out.println("I Blind adesso sono: " + "SB = " + blindsUpdatedEvent.getSmallBlind() + " BB = " + blindsUpdatedEvent.getBigBlind());


        /*
         * Update del creator player (viene riscosso lo Small Blind
         */

        Events stakeEvents = creatorClient.listenForAMessage();
        PotUpdatedEvent potUpdatedEvent = (PotUpdatedEvent) stakeEvents.getEvent();
        PlayerUpdatedEvent creatorUpdate = (PlayerUpdatedEvent) stakeEvents.getEvent();

        System.out.println("New pot: " + potUpdatedEvent.getPot());
        System.out.println("Creator update chips: " + creatorUpdate.getPlayer().getTotalChips());

        /*
         * Update del client player (viene riscosso il BigBlinf
         */

        Events stakeEvents1 = creatorClient.listenForAMessage();
        PotUpdatedEvent potUpdatedEvent1 = (PotUpdatedEvent) stakeEvents1.getEvent();
        PlayerUpdatedEvent clientUpdate = (PlayerUpdatedEvent) stakeEvents1.getEvent();

        System.out.println("New pot: " + potUpdatedEvent1.getPot());
        System.out.println("Client update chips: " + clientUpdate.getPlayer().getTotalChips());


        Events stakeCreator = creatorClient.listenForAMessage();
        //ActionOptionsEvent optionsEvent = (ActionOptionsEvent) stakeCreator.getEvent();

        Events creatorAction = new Events();
        //creatorAction.addEvent(new ActionPerformedEvent(new StakeAction(ActionType.CALL, 10000)));
        creatorClient.sendMessage(creatorAction);

        Events responseToCreator = creatorClient.listenForAMessage();
        PotUpdatedEvent potUpdatedEvent2 = (PotUpdatedEvent) responseToCreator.getEvent();
        PlayerUpdatedEvent creatorUpdate1 = (PlayerUpdatedEvent) responseToCreator.getEvent();

        System.out.println("New pot: " + potUpdatedEvent2.getPot());
        System.out.println("Creator update chips: " + creatorUpdate1.getPlayer().getTotalChips());

        Events stakeClient = creatorClient.listenForAMessage();

        Events responseToClient = creatorClient.listenForAMessage();
        PotUpdatedEvent potUpdatedEvent3 = (PotUpdatedEvent) responseToClient.getEvent();
        PlayerUpdatedEvent clientUpdate1 = (PlayerUpdatedEvent) responseToClient.getEvent();

        System.out.println("New pot: " + potUpdatedEvent3.getPot());
        System.out.println("Creator update chips: " + clientUpdate1.getPlayer().getTotalChips());

    }
}
