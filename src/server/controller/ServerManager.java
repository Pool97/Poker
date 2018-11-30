package server.controller;

import client.events.PlayerConnectedEvent;
import server.events.EventsContainer;
import server.events.PlayerLoggedEvent;
import server.model.automa.Game;
import server.model.Dealer;
import server.model.PlayerModel;
import server.model.Table;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerManager implements Runnable {
    private final static String SERVER_INFO = "SERVER -> ";
    private final static int SERVER_PORT = 4040;
    private final static int MAX_CONNECTION_QUEUE_LENGTH = 8;
    private final static String LISTEN_FOR_CLIENTS_INFO = "IN ATTESA DI CONNESSIONI... \n";
    private final static String CLIENT_CONNECTED_INFO = "CONNESSO CON";
    private final static String SERVER_ERROR = "I/O ERROR. ";
    private final static String SERVER_SHUTDOWN_INFO = "STO TERMINANDO IL SERVER...\n";
    private final static String PLAYER_ADDED = "GIOCATORE AGGIUNTO ALLA LISTA PER LA PARTITA IMMINENTE... \n";

    private final static Logger logger = Logger.getLogger(ServerManager.class.getName());
    private int totalPlayers;
    private ServerSocket serverSocket;
    private Table table;
    private Game game;

    public ServerManager(int totalPlayers) {
        try {
            serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTION_QUEUE_LENGTH);
            initializeModelComponents();
            game = new Game(table);
            this.totalPlayers = totalPlayers;
        } catch (IOException e) {
            logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
        }
    }

    private void initializeModelComponents(){
        table = new Table();
        Dealer dealer = new Dealer();
        dealer.setTable(table);
        table.setDealer(dealer);
    }

    public void run() {
        do
            listen();
        while (true);
    }

    private void listen(){
        logger.info(SERVER_INFO + LISTEN_FOR_CLIENTS_INFO);
        try {
            Socket socket = serverSocket.accept();

            logger.info(SERVER_INFO + CLIENT_CONNECTED_INFO + socket.getInetAddress() + "\n");

            handleClient(socket);

            updateLobbyList();

            if (isMatchReadyToStart())
                new Thread(game).start();

        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
            Thread.currentThread().interrupt();
        }
    }

    private void handleClient(Socket socket){
        BlockingQueue<EventsContainer> queue1 = new ArrayBlockingQueue<>(20);
        BlockingQueue<EventsContainer> queue2 = new ArrayBlockingQueue<>(20);

        ClientSocket client = new ClientSocket(socket, queue1, queue2);
        PlayerModel model = new PlayerModel();

        table.sit(model);

        new Thread(client).start();

        initializePlayerModel(model, queue2);

        ConcreteReceiver receiver = new ConcreteReceiver(model.getNickname(), queue2, queue1);
        game.register(receiver);
        receiver.register(client);
    }

    private void initializePlayerModel(PlayerModel model, BlockingQueue<EventsContainer> queue2){
        EventsContainer newPlayerConnected = null;
        try {
            newPlayerConnected = queue2.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PlayerConnectedEvent event = (PlayerConnectedEvent) newPlayerConnected.getEvent();

        logger.info(PLAYER_ADDED + event.getNickname());

        model.setNickname(event.getNickname());
        model.setAvatar(event.getAvatar());
        model.setCreator(table.currentNumberOfPlayers() == 0);
    }

    private void updateLobbyList(){
        EventsContainer playersListEvent = new EventsContainer();
        table.getPlayers().forEach(playerModel1 -> playersListEvent.addEvent(
                new PlayerLoggedEvent(playerModel1.getNickname(), playerModel1.getAvatar())));
        game.sendMessage(playersListEvent);
    }

    public boolean isMatchReadyToStart(){
        return table.currentNumberOfPlayers() == totalPlayers;
    }
}

