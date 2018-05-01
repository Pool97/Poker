package server.model;

import events.PlayerCreatedEvent;
import interfaces.Message;
import server.socket.ServerManager;
import utils.RequestHandler;
import utils.RequestSender;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private final ExecutorService poolExecutor = Executors.newCachedThreadPool();
    private int size;

    /**
     * Costruttore vuoto di Room.
     */

    public Room() {
        players = new HashMap<>();
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

    public ArrayList<PlayerModel> getOrderedPlayers() {
        ArrayList<PlayerModel> playersOrdered = new ArrayList<>(players.keySet());
        playersOrdered.sort(Comparator.comparing(PlayerModel::getPosition));
        return playersOrdered;
    }

    public ArrayList<PlayerModel> getPlayers() {
        return new ArrayList<>(players.keySet());
    }

    /**
     * Permette di restituire la connessione relativa al Player argomento del metodo.
     *
     * @param player Player
     * @return Connessione del Player
     */

    public Socket getSocket(PlayerModel player) {
        return players.get(player);
    }

    /**
     * Permette di restituire le connessioni di tutti i Players sotto forma di ArrayList.
     *
     * @return Connessioni di tutti i Players.
     */

    public ArrayList<Socket> getSockets() {
        return new ArrayList<>(players.values());
    }

    /**
     * Permette di traslare di una posizione tutti i Players del Match.
     * Esempio: se alla fine del turno un Player era il D (= Dealer) nel prossimo turno diventerà SB
     * ( = Small Blind) e così via per tutti i Players.
     */

    public void movePlayersPosition(PositionManager positionManager) {
        players.keySet()
                .forEach(player -> player.setPosition(positionManager.nextPosition(player.getPosition())));
    }

    /**
     * Permette di restituire il Player relativo alla posizione specifica.
     *
     * @param position Posizione del Player
     * @return Player
     */

    public PlayerModel getPlayer(Position position) {
        return getOrderedPlayers().stream().filter(player -> player.getPosition() == position).findFirst().get();
    }

    public void setPlayersChips(int chips) {
        players.keySet().forEach(player -> player.setTotalChips(chips));
    }

    /**
     * Permette di rimanere in ascolto su un altro Thread di un qualsiasi messaggio informativo inviato da un qualsiasi Client.
     *
     * @param socket Socket relativo al Player che si vuole ascoltare.
     * @param <T>    Il messaggio da ascoltare dovrà essere un qualsiasi tipo di messaggio conforme all'interfaccia Message.
     * @return Il messaggio inviato dal Client.
     */

    public <T extends Message> T listenForAMessage(Socket socket) {
        T message = null;
        try {
            message = poolExecutor.submit(new RequestHandler<T>(socket, ServerManager.logger)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * Permette di inviare un messaggio a un Client specifico su un altro Thread.
     *
     * @param socket  Connessione con il Client.
     * @param message Messaggio da inviare
     * @param <T>     Tipo di messaggio da inviare (tutti devono implementare {@link Message})
     */

    public <T extends Message> void sendMessage(Socket socket, T message) {
        poolExecutor.submit(new RequestSender<>(socket, message, ServerManager.logger));
    }

    /**
     * Versione di {@link Room#sendMessage(Socket, Message)} temporizzata.
     * Questa si differenzia dalla sopraccitata per il fatto che il Thread chiamante viene bloccato
     * in attesa che il messaggio sia andato a buon fine.
     * <p>
     * //@param socket  Connessione con il Client.
     *
     * @param waiter  Temporizzatore
     * @param message Messaggio da inviare
     * @param <T>     Tipo di messaggio da inviare (tutti devono implementare {@link Message}
     */

    public <T extends Message> void sendMessage(PlayerModel player, CountDownLatch waiter, T message) {
        try {
            poolExecutor.submit(new RequestSender<>(getSocket(player), message, ServerManager.logger)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        waiter.countDown();
    }

    /**
     * Permette di propagare un messaggio in Multicast ai Clients. (se l'ArrayList contiene tutti i Socket
     * dei giocatori diventa banalmente un messaggio Broadcast).
     * Per mantenere efficienza si utilizza un ExecutorService cached in modo da poter inviare i messaggi
     * su più Thread. L'ordine in cui l'invia non è di importanza.
     * È temporizzato, quindi viene attesa la fine della consegna di tutti i messaggi prima di sbloccare
     * il Thread chiamante.
     * <p>
     * //@param sockets Le connessioni dei Clients a cui si vuole inviare un messaggio
     *
     * @param waiter  Temporizzatore
     * @param message Messaggio da propagare
     * @param <T>     Tipo di messaggio da inviare (tutti devono implementare {@link Message}
     */
    public <T extends Message> void sendBroadcast(CountDownLatch waiter, T message) {
        for (Socket socket : players.values()) {
            try {
                poolExecutor.submit(new RequestSender<>(socket, message, ServerManager.logger)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        waiter.countDown();
    }
}
