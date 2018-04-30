import client.socket.ClientManager;
import events.*;

public class ClientSideTest {
    public static void main(String[] args) {
        Events clientEvents = new Events();
        clientEvents.addEvent(new PlayerCreatedEvent("Perry97", "creator.png"));
        ClientManager client = new ClientManager("localhost", 4040);
        client.attemptToConnect();
        client.sendMessage(clientEvents);
        Events playersList = client.listenForAMessage();

        /*
            ==================START TURN ====================
         */
        Events startTurn = client.listenForAMessage();

        while (!playersList.isEmpty()) {
            PlayerAddedEvent playerEvent = (PlayerAddedEvent) playersList.getEvent();
            System.out.println(playerEvent.getNickname() + " " + playerEvent.getPosition());
        }

        BlindsUpdatedEvent blindsUpdatedEvent = (BlindsUpdatedEvent) startTurn.getEvent();
        System.out.println(blindsUpdatedEvent.getSmallBlind() + " " + blindsUpdatedEvent.getBigBlind());

        Events stakeEvents = client.listenForAMessage();
        PotUpdatedEvent potUpdatedEvent = (PotUpdatedEvent) stakeEvents.getEvent();
        PlayerUpdatedEvent creatorUpdate = (PlayerUpdatedEvent) stakeEvents.getEvent();

        System.out.println("New pot: " + potUpdatedEvent.getPot());
        System.out.println(creatorUpdate.getPlayer().getNickname() + " update chips: " + creatorUpdate.getPlayer().getTotalChips());

        Events stakeEvents1 = client.listenForAMessage();
        PotUpdatedEvent potUpdatedEvent1 = (PotUpdatedEvent) stakeEvents1.getEvent();
        PlayerUpdatedEvent clientUpdate = (PlayerUpdatedEvent) stakeEvents1.getEvent();

        System.out.println("New pot: " + potUpdatedEvent1.getPot());
        System.out.println(clientUpdate.getPlayer().getNickname() + " update chips: " + clientUpdate.getPlayer().getTotalChips());

        /*
               =============== END START TURN ================
         */

        Events stakeCreator = client.listenForAMessage();
        ActionOptionsEvent optionsAvailable = (ActionOptionsEvent) stakeCreator.getEvent();
        /*for(StakeAction action : optionsAvailable.getOptions()){
            System.out.println(action.getType() + " " + action.getStakeChips());
        }*/

        Events clientAction = new Events();
        //clientAction.addEvent(new ActionPerformedEvent(new StakeAction(ActionType.CALL, 400)));
        client.sendMessage(clientAction);

        Events responseToCreator = client.listenForAMessage();
        PotUpdatedEvent potUpdatedEvent2 = (PotUpdatedEvent) responseToCreator.getEvent();
        PlayerUpdatedEvent creatorUpdate1 = (PlayerUpdatedEvent) responseToCreator.getEvent();

        System.out.println("New pot: " + potUpdatedEvent2.getPot());
        System.out.println("Creator update chips: " + creatorUpdate1.getPlayer().getTotalChips());

        Events stakeClient = client.listenForAMessage();


        Events responseToClient = client.listenForAMessage();
        PotUpdatedEvent potUpdatedEvent3 = (PotUpdatedEvent) responseToClient.getEvent();
        PlayerUpdatedEvent clientUpdate1 = (PlayerUpdatedEvent) responseToClient.getEvent();

        System.out.println("New pot: " + potUpdatedEvent3.getPot());
        System.out.println("Creator update chips: " + clientUpdate1.getPlayer().getTotalChips());
    }
}
