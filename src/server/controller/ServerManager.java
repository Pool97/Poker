package server.controller;

import client.event.EventsAdapter;
import client.event.MatchCanStart;
import client.event.MatchMode;
import client.event.PlayerConnected;
import interfaces.ClientEvent;
import interfaces.Event;
import interfaces.Observer;
import server.events.ChatMessage;
import server.events.LoggingStatus;
import server.events.PlayerDisconnected;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerManager implements Runnable, Observer {
    private final static String SERVER_INFO = "SERVER -> ";
    private final static int SERVER_PORT = 4040;
    private final static int MAX_CONNECTION_QUEUE_LENGTH = 8;
    private final static String LISTEN_FOR_CLIENTS_INFO = "IN ATTESA DI CONNESSIONI... \n";
    private final static String CLIENT_CONNECTED_INFO = "CONNESSO CON";
    private final static String SERVER_SHUTDOWN_INFO = "STO TERMINANDO IL SERVER...\n";
    private final static String PLAYER_ADDED = "GIOCATORE AGGIUNTO ALLA LISTA PER LA PARTITA IMMINENTE... \n";

    private final static Logger logger = Logger.getLogger(ServerManager.class.getName());
    private ServerSocket serverSocket;
    private BlockingQueue<ChatMessage> chatQueue = new ArrayBlockingQueue<>(40);
    private Table table;
    private Game game;
    private ServerEventManager eventManager;

    public ServerManager() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTION_QUEUE_LENGTH);
            eventManager = new ServerEventManager();
            initializeModelComponents();
            game = new Game(table);
        } catch (IOException e) {
            logger.log(Level.WARNING, SERVER_SHUTDOWN_INFO);
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

        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, SERVER_SHUTDOWN_INFO);
            Thread.currentThread().interrupt();
        }

    }

    private void handleClient(Socket socket){
        BlockingQueue<Event> writeQueue = new ArrayBlockingQueue<>(20);
        ClientSocket client = new ClientSocket(socket, writeQueue, chatQueue);
        client.register(this);
        new Thread(client).start();
        client.update(new LoggingStatus(!game.isRunning()));

        if (!game.isRunning()) {
            ConcreteReceiver receiver = new ConcreteReceiver(writeQueue);
            receiver.register(client);
            game.register(receiver);
        }

    }

    private void updateLobbyList(){
        for(PlayerModel player : table)
            game.sendMessage(new PlayerLogged(player.getNickname(), player.getAvatar()));
    }

    @Override
    public synchronized void update(Event event) {
        ((ClientEvent)event).accept(eventManager);
    }

    class ServerEventManager extends EventsAdapter{

        @Override
        public void process(MatchCanStart event) {
            new Thread(game).start();
            ChatThread chat = new ChatThread(chatQueue);
            chat.register(game);
            new Thread(chat).start();
        }

        @Override
        public void process(PlayerDisconnected event) {
            game.sendMessage(event);
            table.getPlayerByName((event).getNickname()).setDisconnected(true);
            table.removeDisconnectedPlayers();
        }

        @Override
        public void process(MatchMode event) {
            game.setBettingStructure(event.isFixedLimit() ? new FixedLimit(10) : new NoLimit(20));
            game.setGameType(new Tournament(game.getSmallBlind()));
        }

        @Override
        public void process(PlayerConnected event) {
            if (!game.isRunning()) {
                PlayerModel model = new PlayerModel();
                model.setNickname(event.getNickname());
                model.setAvatar(event.getAvatar());
                model.setCreator(table.currentNumberOfPlayers() == 0);
                table.sit(model);
                logger.info(PLAYER_ADDED + event.getNickname());
                updateLobbyList();
            }
        }
    }
}

