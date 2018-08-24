package server.automa;

import interfaces.PokerState;
import server.events.BlindsUpdatedEvent;
import server.events.Events;
import server.events.PlayerHasWinEvent;
import server.events.TurnStartedEvent;
import server.model.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * StartTurn è il secondo stato dell'automa.
 * Esso può iniziare per via di due transizioni:
 * - Transizione da StartGame: StartTurn rappresenta il primo turno della partita di Poker.
 * - Transizione da Showdown: StartTurn rappresenta un nuovo turno della partita di Poker, subito
 * dopo la conclusione del precedente, conclusosi con lo stato di Showdown.
 * <p>
 * In questo stato devono essere compiute le seguenti azioni:
 * - creazione e mescolamento del mazzo di carte;
 * - riazzeramento del pot (per evitare di continuare a utilizzare il pot del turno precedente)
 * - incremento dei bui
 * - Informare tutti i players dell'aggiornamento del valore dei bui
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class StartTurn implements PokerState {
    private final static String STATE_STARTED = "Lo stato di StartTurn è avviato. \n";
    private final static String CONF_TURN = "Configurazione dei parametri del turno in corso... \n";

    private MatchHandler match;
    private TurnModel turnModel;
    private MatchModel matchModel;
    private Room room;


    public StartTurn(MatchHandler match) {
        this.match = match;
        turnModel = match.getTurnModel();
        matchModel = match.getMatchModel();
        room = match.getRoom();
    }


    @Override
    public void goNext() {
        MatchHandler.logger.info(STATE_STARTED);
        MatchHandler.logger.info(CONF_TURN);

        checkIfMatchEnded();

        updateMatchParameters();
        updateRoom();
        dealCards();

        sendNewBlindsToPlayers();

        room.sendBroadcast(prepareTurnStartedEvents());

        match.setState(new SmallBlind(match));
    }

    private void checkIfMatchEnded() {
        if (room.hasWinner())
            room.sendBroadcast(new Events(new PlayerHasWinEvent(room.getWinner())));
    }

    private void updateRoom() {
        room.removeDisconnectedPlayers();
        room.movePlayersPosition();
        //room.getPlayers().forEach(playerModel -> System.out.println(playerModel.getNickname() + " " + playerModel.getPosition().name()));
    }

    private void updateMatchParameters() {
        turnModel.createDeck();
        turnModel.resetPot();
        turnModel.emptyCommunity();
        matchModel.increaseBlinds();

    }

    private void sendNewBlindsToPlayers() {
        room.sendBroadcast(new Events(new BlindsUpdatedEvent(matchModel.getSmallBlind(), matchModel.getBigBlind())));
    }

    private Events prepareTurnStartedEvents() {
        ArrayList<TurnStartedEvent> events = new ArrayList<>();
        for (PlayerModel player : room.getPlayers())
            events.add(new TurnStartedEvent(player.getNickname(), player.getPosition().name(),
                    player.getCards().stream().map(CardModel::getImageDirectoryPath).collect(Collectors.toCollection(ArrayList::new))));
        return new Events(events);
    }

    private void dealCards() {
        room.getPlayers().stream().filter(playerModel -> !playerModel.hasLost()).forEach(player -> player.addCard(turnModel.getNextCard()));
        room.getPlayers().stream().filter(playerModel -> !playerModel.hasLost()).forEach(player -> player.addCard(turnModel.getNextCard()));
    }
}