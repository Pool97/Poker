package server.model;

import interfaces.Message;
import server.socket.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Classe che permette di modellizzare il funzionamento di una stanza da gioco.
 * Si occupa della comunicazione con i Players e di effettuare tutte le modifiche necessarie ai Model dei Players.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Room {
    private ArrayList<Player> players;
    private PositionManager positionManager;
    private final ExecutorService poolExecutor = Executors.newCachedThreadPool();

    /**
     * Costruttore vuoto di Room.
     */

    public Room() {
        players = new ArrayList<>();
        positionManager = new PositionManager();
    }

    /**
     * Permette di aggiungere un nuovo giocatore alla stanza.
     * @param player Il PlayerBoard
     */

    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Permette di ritornare la dimensione della stanza, ossia il numero di Players presenti in una determinata fase di gioco.
     *
     * @return Numero di players presenti nella stanza
     */

    public int getSize() {
        return players.size();
    }

    /**
     * Permette di effettuare un riordinamento della stanza in base alla posizione del turno che ricoprono i Players.
     *
     * @return Model di tutti i Players.
     */

    public void sort() {
        players.sort(Comparator.comparingInt(client -> client.getPlayerModel().getPosition().ordinal()));
    }

    public ArrayList<PlayerModel> getPlayers() {
        return players.stream().map(Player::getPlayerModel).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Permette di traslare di una posizione tutti i Players del MatchHandler.
     * Esempio: se alla fine del turno un PlayerBoard era il D (= Dealer) nel prossimo turno diventerà SB
     * ( = Small Blind) e così via per tutti i Players.
     */

    //TODO: da rivedere...
    public void movePlayersPosition(PositionManager positionManager) {
        //players.keySet()
        // .forEach(player -> player.setPosition(positionManager.nextPosition(player.getPosition())));
    }

    /**
     * Permette di restituire il PlayerBoard relativo alla posizione da Poker specifica.
     *
     * @param position Posizione del PlayerBoard
     * @return PlayerBoard
     */

    public PlayerModel getPlayer(Position position) {
        return getPlayers().stream().filter(player -> player.getPosition() == position).findFirst().get();
    }

    /**
     * Permette di impostare a tutti i players lo stesso quantitativo di Chips.
     *
     * @param chips Quantitativo di Chips da impostare
     */

    public void setPlayersChips(int chips) {
        players.forEach(client -> client.getPlayerModel().setChips(chips));
    }

    /**
     * Permette di impostare a tutti i players le posizioni da Poker specificate dall'ArrayList.
     */

    public void setPlayersPositions() {
        for (int i = 0; i < getSize(); i++)
            players.get(i).getPlayerModel().setPosition(positionManager.getPositions().get(i));
    }

    public void setAvailablePositions(int size) {
        positionManager.addPositions(size);
    }

    public Position getNextPosition(Position oldPosition) {
        return positionManager.nextPosition(oldPosition);
    }

    /**
     * Permette di rimanere in ascolto su un Thread separato per un qualsiasi messaggio inviato dal Client specificato attraverso il suo Model.
     *
     * @param playerModel Il PlayerBoard da cui ricevere il messaggio
     * @param <T>         Tipo di messaggio da ricevere (vedi {@link Message})
     */

    public <T extends Message> T readMessage(PlayerModel playerModel) {
        T message;
        Player player = players.stream().filter(client -> client.getPlayerModel().equals(playerModel)).findFirst().get();
        message = player.readMessage(poolExecutor);
        return message;
    }

    /**
     * Permette di rimanere in ascolto su un Thread separato per un qualsiasi messaggio inviato dal Client specificato attraverso {@link Player}.
     * @param player Il PlayerBoard da cui ricevere il messaggio
     * @param <T> Tipo di messaggio da ricevere (vedi {@link Message})
     */

    public <T extends Message> T readMessage(Player player) {
        return player.readMessage(poolExecutor);
    }

    /**
     * Permette di propagare un messaggio a un singolo player.
     *
     * @param player Il PlayerBoard a cui inviare il messaggio
     * @param message Messaggio da inviare
     * @param <T> Tipo di messaggio da inviare (vedi {@link Message})
     */

    public <T extends Message> void sendMessage(PlayerModel player, T message) {
        Player clientSocket = players.stream().filter(client -> client.getPlayerModel().equals(player)).findFirst().orElse(null);
        clientSocket.sendMessage(poolExecutor, message);
    }

    /**
     * Permette di propagare un messaggio in Broadcast a tutti i players.
     * Per mantenere efficienza si utilizza un ExecutorService cached in modo da poter inviare i messaggi
     * su più Thread. L'ordine in cui vengono propagati non è di particolare importanza.
     *
     * @param message Messaggio da propagare
     * @param <T>     Tipo di messaggio da inviare (tutti devono implementare {@link Message}
     */

    public <T extends Message> void sendBroadcast(T message) {
        players.forEach(player -> player.sendMessage(poolExecutor, message));
    }
}