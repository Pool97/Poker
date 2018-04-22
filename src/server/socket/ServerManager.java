package server.socket;

import client.messages.CreationRoomMessage;
import interfaces.Message;
import server.model.Room;
import utils.RequestHandler;
import utils.RequestSender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Classe che permette di gestire l'intera infrastruttura di rete lato Server.
 * È la principale responsabile del collegamento tra i Client e il Server.
 *
 * @author Nipuna Perera
 * @author Roberto Poletti
 * @since 1.0
 */

public class ServerManager implements Runnable {
    public final static String SERVER_INFO = "SERVER -> ";
    public final static int SERVER_PORT = 4040;
    public final static int MAX_CONNECTION_QUEUE_LENGTH = 8;
    private final static String LISTEN_FOR_CLIENTS_INFO = "IN ATTESA DI CONNESSIONI... \n";
    private final static String CLIENT_CONNECTED_INFO = "CONNESSO CON";
    private final static String SERVER_ERROR = "I/O ERROR. ";
    private final static String WAITING_FOR_INFO = "IN ATTESA DI ALTRI ";
    private final static String PLAYERS = " GIOCATORI \n";
    private final static String SERVER_SHUTDOWN_INFO = "SHUTTING DOWN THE SERVER...\n";
    private final static String PLAYER_ADDED = "GIOCATORE AGGIUNTO ALLA LISTA PER LA PARTITA IMMINENTE... \n";
    private final ExecutorService poolExecutor = Executors.newCachedThreadPool();
    private final Logger logger = Logger.getLogger(ServerManager.class.getName());
    private ServerSocket serverSocket;
    private CountDownLatch roomCreationSignal;
    private Room room;

    /**
     * Costruttore vuoto di ServerManager.
     * Viene istanziato il ServerSocket e costruita la stanza necessaria per accogliere i Players che
     * si collegano al Server.
     */

    public ServerManager(CountDownLatch roomCreationSignal) {
        try {
            serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTION_QUEUE_LENGTH);
            this.roomCreationSignal = roomCreationSignal;
            room = new Room();
        } catch (IOException e) {
            logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
        }
    }

    /**
     * Vedi {@link Runnable#run()}
     */

    public void run() {
        do
            listen();
        while (true);
    }

    /**
     * Permette di aggiungere alla stanza ogni Players che si collega.
     * Il primo Client deve essere necessariamente il creatore della stanza, che fornisce la dimensione
     * che dovrà avere la stanza (equivalentemente fornisce il numero di giocatori iniziali della partita).
     */

    private void listen() {
        logger.info(SERVER_INFO + LISTEN_FOR_CLIENTS_INFO);

        try {
            Socket socket = serverSocket.accept();
            logger.info(SERVER_INFO + CLIENT_CONNECTED_INFO + socket.getInetAddress() + "\n");

            if (room.getNumberOfPlayers() == 0) {
                CreationRoomMessage message = listenForAMessage(socket);
                room.setSize(message.getMaxPlayers());
                logger.info(SERVER_INFO + WAITING_FOR_INFO + (room.getSize() - 1) + PLAYERS);
            }

            room.addPlayer(listenForAMessage(socket), socket);
            logger.info(PLAYER_ADDED);

            if (room.getSize() == room.getNumberOfPlayers()) {
                roomCreationSignal.countDown();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Restituisce la stanza dei Players.
     *
     * @return Stanza
     */

    public Room getRoom() {
        return room;
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
            message = poolExecutor.submit(new RequestHandler<T>(socket, logger)).get();
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
        poolExecutor.submit(new RequestSender<>(socket, message, logger));
    }

    /**
     * Versione di {@link ServerManager#sendMessage(Socket, Message)} temporizzata.
     * Questa si differenzia dalla sopraccitata per il fatto che il Thread chiamante viene bloccato
     * in attesa che il messaggio sia andato a buon fine.
     *
     * @param socket  Connessione con il Client.
     * @param waiter  Temporizzatore
     * @param message Messaggio da inviare
     * @param <T>     Tipo di messaggio da inviare (tutti devono implementare {@link Message}
     */

    public <T extends Message> void sendMessage(Socket socket, CountDownLatch waiter, T message) {
        try {
            poolExecutor.submit(new RequestSender<>(socket, message, logger)).get();
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
     *
     * @param sockets Le connessioni dei Clients a cui si vuole inviare un messaggio
     * @param waiter  Temporizzatore
     * @param message Messaggio da propagare
     * @param <T>     Tipo di messaggio da inviare (tutti devono implementare {@link Message}
     */
    public <T extends Message> void sendMessage(ArrayList<Socket> sockets, CountDownLatch waiter, T message) {
        for (Socket socket : sockets) {
            try {
                poolExecutor.submit(new RequestSender<>(socket, message, logger)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        waiter.countDown();
    }
}

