package client;

import interfaces.Message;
import utils.RequestSender;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Classe che permette di gestire l'infrastruttura di rete lato Client.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class ClientSocketManager {
    private Socket socket;
    private String serverName;
    private int serverPort;
    private final static ExecutorService executor = Executors.newSingleThreadExecutor();

    private final static Logger logger = Logger.getLogger(ClientSocketManager.class.getName());
    private final static String CLIENT_INFO = "CLIENT -> ";
    private final static String CONNECTING_INFO = " STA CERCANDO DI STABILIRE UNA CONNESSIONE VERSO IL SERVER \n";
    private final static String CONNECTION_ESTABLISHED_INFO = " CONNESSIONE AVVENUTA VERSO IL SERVER ";
    private final static String PORT_INFO = " ALLA PORTA ";
    private final static String SERVICE_INTERRUPTED = " STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";

    /**
     * Costruttore della classe ClientSocketManager.
     *
     * @param serverName Nome del Server a cui connettersi
     * @param serverPort Porta del Server a cui connettersi
     */

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

    /**
     * Permette di inviare un messaggio al Server su un altro Thread.
     * @param message Messaggio da inviare
     * @param <T> Tipo di messaggio da inviare (tutti devono implementare {@link Message}
     */

    public <T extends Message> void sendMessage(T message){
        executor.submit(new RequestSender<>(socket, message, logger));
    }
}
