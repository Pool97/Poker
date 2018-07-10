package server.socket;

import client.events.CreatorConnectedEvent;
import client.events.PlayerConnectedEvent;
import server.events.Events;
import server.events.PlayerLoggedEvent;
import server.model.PlayerModel;
import server.model.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Classe che permette di gestire il collegamento iniziale dei Players al Server.
 *
 * @author Nipuna Perera
 * @author Roberto Poletti
 * @since 1.0
 */

public class ServerManager implements Runnable {
    private final static String SERVER_INFO = "SERVER -> ";
    private final static int SERVER_PORT = 4040;
    private final static int MAX_CONNECTION_QUEUE_LENGTH = 8;
    private final static String LISTEN_FOR_CLIENTS_INFO = "IN ATTESA DI CONNESSIONI... \n";
    private final static String CLIENT_CONNECTED_INFO = "CONNESSO CON";
    private final static String SERVER_ERROR = "I/O ERROR. ";
    private final static String WAITING_FOR_INFO = "IN ATTESA DI ALTRI ";
    private final static String PLAYERS = " GIOCATORI \n";
    private final static String SERVER_SHUTDOWN_INFO = "STO TERMINANDO IL SERVER...\n";
    private final static String PLAYER_ADDED = "GIOCATORE AGGIUNTO ALLA LISTA PER LA PARTITA IMMINENTE... \n";

    private final static Logger logger = Logger.getLogger(ServerManager.class.getName());
    private int totalPlayers;
    private ServerSocket serverSocket;
    private CountDownLatch roomCreationSignal;
    private Room room;

    /**
     * Costruttore vuoto di ServerManager.
     * Viene istanziato il ServerSocket e costruita la stanza necessaria per accogliere i Players che
     * si collegano al Server.
     */

    public ServerManager(Room room, CountDownLatch roomCreationSignal) {
        try {
            serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTION_QUEUE_LENGTH);
            this.roomCreationSignal = roomCreationSignal;
            this.room = room;
        } catch (IOException e) {
            logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
        }
    }

    /**
     * {@link Runnable#run()}
     */

    public void run() {
        do
            listen();
        while (true);
    }

    /**
     * Permette di aggiungere alla stanza ogni Players che si collega.
     * Il primo Client deve essere necessariamente  PlayerBoard che deve puntare attualmente il creatore della stanza, che
     * fornisce la dimensione che dovrà avere la stanza (equivalentemente fornisce il numero di giocatori iniziali
     * della partita).
     */

    private void listen() {
        logger.info(SERVER_INFO + LISTEN_FOR_CLIENTS_INFO);

        try {
            Socket socket = serverSocket.accept();
            logger.info(SERVER_INFO + CLIENT_CONNECTED_INFO + socket.getInetAddress() + "\n");

            Player player = new Player(socket);
            Events newPlayer = room.readMessage(player);

            if (room.getSize() == 0) {
                totalPlayers = ((CreatorConnectedEvent) newPlayer.getEvent()).getTotalPlayers();
                logger.info(SERVER_INFO + WAITING_FOR_INFO + (totalPlayers - 1) + PLAYERS);
            }

            PlayerConnectedEvent event = (PlayerConnectedEvent) newPlayer.getEvent();
            PlayerModel playerModel = new PlayerModel(event.getNickname(), event.getAvatar());
            player.setPlayerModel(playerModel);
            room.addPlayer(player);
            Events playersListEvent = new Events();
            room.getPlayers().forEach(playerModel1 -> playersListEvent.addEvent(new PlayerLoggedEvent(playerModel1.getNickname())));
            room.sendBroadcast(playersListEvent);
            logger.info(PLAYER_ADDED + event.getNickname());

            if (room.getSize() == totalPlayers) {
                roomCreationSignal.countDown();
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
            Thread.currentThread().interrupt();
        }
    }
}

