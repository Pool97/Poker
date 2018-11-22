package server.controller;

import client.events.PlayerConnectedEvent;
import server.events.EventsContainer;
import server.events.PlayerLoggedEvent;
import server.model.PlayerModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
    private Room room;

    public ServerManager(int totalPlayers) {
        try {
            serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTION_QUEUE_LENGTH);
            room = new Room();
            this.totalPlayers = totalPlayers;
        } catch (IOException e) {
            logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
        }
    }

    public void run() {
        do
            listen();
        while (true);
    }

    private void listen() {
        logger.info(SERVER_INFO + LISTEN_FOR_CLIENTS_INFO);

        try {
            Socket socket = serverSocket.accept();
            logger.info(SERVER_INFO + CLIENT_CONNECTED_INFO + socket.getInetAddress() + "\n");

            PlayerController player = new PlayerController(socket);
            EventsContainer newPlayer = room.readMessage(player);

            PlayerConnectedEvent event = (PlayerConnectedEvent) newPlayer.getEvent();
            PlayerModel playerModel = new PlayerModel(event.getNickname(), event.getAvatar());
            playerModel.setCreator(room.getSize() == 0);
            player.setPlayerModel(playerModel);
            room.addPlayer(player);
            EventsContainer playersListEvent = new EventsContainer();
            room.getPlayers().forEach(playerModel1 -> playersListEvent.addEvent(new PlayerLoggedEvent(playerModel1.getNickname(), playerModel1.getAvatar())));
            room.sendBroadcast(playersListEvent);
            logger.info(PLAYER_ADDED + event.getNickname());

            if (room.getSize() == totalPlayers) {
                new Thread(new Context(room)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
            Thread.currentThread().interrupt();
        }
    }
}

