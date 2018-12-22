package server.controller;

import client.events.MatchMode;
import client.events.PlayerConnected;
import interfaces.Event;
import server.events.ChatMessage;
import server.events.PlayerLogged;
import server.model.Dealer;
import server.model.PlayerModel;
import server.model.Table;
import server.model.gamestructure.FixedLimit;
import server.model.gamestructure.NoLimit;
import server.model.gamestructure.Tournament;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ListIterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
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
    private ServerSocket serverSocket;
    private BlockingQueue<ChatMessage> chatQueue = new ArrayBlockingQueue<>(40);
    private Table table;
    private Game game;
    private CountDownLatch latch;

    public ServerManager() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTION_QUEUE_LENGTH);
            initializeModelComponents();
            ChatThread chat = new ChatThread(chatQueue);
            new Thread(chat).start();
            latch = new CountDownLatch(1);
            game = new Game(table, latch);
            chat.register(game);
            new Thread(game).start();
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

        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
            Thread.currentThread().interrupt();
        }

    }

    private void handleClient(Socket socket){
        BlockingQueue<Event> writeQueue = new ArrayBlockingQueue<>(20);
        ClientSocket client = new ClientSocket(socket, writeQueue, chatQueue, latch);

        new Thread(client).start();

        if(table.size() == 0){
            Event event = client.readQueue();
            MatchMode matchMode = (MatchMode) event;
            game.setBettingStructure(matchMode.isFixedLimit() ? new FixedLimit(10) : new NoLimit(20));
            game.setGameType(new Tournament(game.getSmallBlind()));
        }

        PlayerConnected playerProperties = (PlayerConnected)client.readQueue();

        logger.info(PLAYER_ADDED + playerProperties.getNickname());

        PlayerModel model = new PlayerModel();
        model.setNickname(playerProperties.getNickname());
        model.setAvatar(playerProperties.getAvatar());
        model.setCreator(table.currentNumberOfPlayers() == 0);
        table.sit(model);

        ConcreteReceiver receiver = new ConcreteReceiver(model.getNickname(), writeQueue);
        game.register(receiver);
        receiver.register(client);
    }

    private void updateLobbyList(){
        ListIterator<PlayerModel> iterator = table.iterator();
        PlayerModel player;

        while(iterator.hasNext()){
            player = iterator.next();
            game.sendMessage(new PlayerLogged(player.getNickname(), player.getAvatar()));
        }
    }
}

