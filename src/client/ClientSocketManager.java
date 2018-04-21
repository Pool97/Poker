package client;

import server.interfaces.Message;
import server.socket.WelcomeCreatorMessage;
import server.socket.WelcomePlayerMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientSocketManager {
    private Socket socket;
    private String serverName;
    private int serverPort;

    private final static Logger logger = Logger.getLogger(ClientSocketManager.class.getName());
    private final static String CLIENT_INFO = "CLIENT -> ";
    private final static String CONNECTING_INFO = " STA CERCANDO DI STABILIRE UNA CONNESSIONE VERSO IL SERVER \n";
    private final static String CONNECTION_ESTABLISHED_INFO = " CONNESSIONE AVVENUTA VERSO IL SERVER ";
    private final static String PORT_INFO = " ALLA PORTA ";
    private final static String SERVICE_INTERRUPTED = " STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";

    public ClientSocketManager(String serverName, int serverPort){
        this.serverName = serverName;
        this.serverPort = serverPort;
        attemptToConnect();
    }

    private void attemptToConnect(){
        logger.info(CLIENT_INFO + CONNECTING_INFO);
        try {
            socket = new Socket(serverName, serverPort);
            logger.info(CLIENT_INFO + CONNECTION_ESTABLISHED_INFO + serverName + PORT_INFO +  serverPort + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(CLIENT_INFO  + SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
    }

    public <T extends Message> void sendMessage(T message){
        new Thread(new RequestSender<>(socket, message)).start();
    }

    public static void main(String [] args){
        WelcomeCreatorMessage creatorMessage = new WelcomeCreatorMessage("Pool97", 2, 20000, "creator.png");
        WelcomePlayerMessage perryMessage = new WelcomePlayerMessage("Perry97", "perry.png");
        WelcomePlayerMessage tunsiMessage = new WelcomePlayerMessage("Tunsi97", "tunsi.png");

        ClientSocketManager creatorClient = new ClientSocketManager("localhost", 4040);
        creatorClient.sendMessage(creatorMessage);

        ClientSocketManager perryClient = new ClientSocketManager("localhost", 4040);
        perryClient.sendMessage(perryMessage);

        ClientSocketManager tunsiClient = new ClientSocketManager("localhost", 4040);
        tunsiClient.sendMessage(tunsiMessage);
        //new Thread(new RequestSender<>("localhost", 4040, creatorMessage)).start();
        ///new Thread(new RequestSender<>("localhost", 4040, perryMessage)).start();
        //new Thread(new RequestSender<>("localhost",4040, tunsiMessage)).start();

    }
}
