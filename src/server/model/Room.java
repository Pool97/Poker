package server.model;

import events.PlayerCreatedEvent;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Classe che permette di modellizzare il funzionamento di una stanza da gioco.
 * Si occupa di mantenere aperte le connessioni ai Players e di effettuare tutte
 * le modifiche necessarie ai Model dei Players.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Room {
    private HashMap<PlayerModel, Socket> players;
    private PositionManager positionManager;
    private int size;

    /**
     * Costruttore vuoto di Room.
     */

    public Room() {
        players = new HashMap<>();
        positionManager = new PositionManager();
    }

    /**
     * Permette di aggiungere un nuovo giocatore alla stanza.
     *
     * @param event Il messaggio contenente le informazioni di base del Player.
     * @param socket        Il collegamento con il Client.
     */

    public void addPlayer(PlayerCreatedEvent event, Socket socket) {
        PlayerModel playerModel = new PlayerModel(event.getNickname(), event.getAvatar());
        players.put(playerModel, socket);
    }

    /**
     * Permette di ritornare la dimensione della stanza, ossia il numero di Players presenti.
     *
     * @return Dimensione della stanza
     */

    public int getSize() {
        return size;
    }

    /**
     * Permette di impostare la dimensione della stanza. Utile all'inizio quando si vuole specificare un limite
     * massimo dei giocatori che si devono connettere alla partita.
     *
     * @param size Dimensione da impostare per la stanza
     */

    public void setSize(int size) {
        this.size = size;
        positionManager.addPositions(size);
    }

    /**
     * Permette di ritornare il numero di Players della stanza. Questo metodo è utile in fase iniziale per monitorare
     * l'accesso dei Clients al Server. Quando tutti i clients si sono connessi, utilizzare questo metodo oppure {@link Room#getSize()}
     * è del tutto indifferente perchè restituiscono lo stesso identico valore.
     *
     * @return Numero di Players presenti nella stanza.
     */

    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * Permette di restituire il Model di tutti i players sotto forma di ArrayList.
     *
     * @return Model di tutti i Players.
     */

    public ArrayList<PlayerModel> getPlayers() {
        ArrayList<PlayerModel> playersOrdered = new ArrayList<>(players.keySet());
        playersOrdered.sort(Comparator.comparing(PlayerModel::getPosition));
        return playersOrdered;
    }

    /**
     * Permette di restituire la connessione relativa al Player argomento del metodo.
     *
     * @param player Player
     * @return Connessione del Player
     */

    public Socket getPlayerSocket(PlayerModel player) {
        return players.get(player);
    }

    /**
     * Permette di restituire le connessioni di tutti i Players sotto forma di ArrayList.
     *
     * @return Connessioni di tutti i Players.
     */

    public ArrayList<Socket> getConnections() {
        return new ArrayList<>(players.values());
    }

    /**
     * Permette di traslare di una posizione tutti i Players del Match.
     * Esempio: se alla fine del turno un Player era il D (= Dealer) nel prossimo turno diventerà SB
     * ( = Small Blind) e così via per tutti i Players.
     */

    public void movePlayersPosition() {
        players.keySet()
                .forEach(player -> player.setPosition(positionManager.nextPosition(player.getPosition())));
    }

    /**
     * Permette di impostare la posizione iniziale di ogni giocatore. Il criterio è che le posizioni vengono
     * assegnate in base all'ordine cronologico di connessione dei Player alla partita.
     */

    public void setInitialPositions() {
        ArrayList<Position> availablePositions = positionManager.getAvailablePositions();
        ArrayList<PlayerModel> playersList = new ArrayList<>(players.keySet());
        for (int i = 0; i < size; i++) {
            playersList.get(i).setPosition(availablePositions.get(i));
        }
    }

    /**
     * Permette di restituire il Player relativo alla posizione specifica.
     *
     * @param position Posizione del Player
     * @return Player
     */

    public PlayerModel getPlayerByPosition(Position position) {
        return getPlayers().stream().filter(player -> player.getPosition() == position).findFirst().get();
    }

    public PositionManager getAvailablePositions() {
        return positionManager;
    }
}
