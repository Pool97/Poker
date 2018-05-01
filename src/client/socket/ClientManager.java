package client.socket;

import interfaces.Message;
import utils.RequestHandler;
import utils.RequestSender;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
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

public class ClientManager {
    private Socket socket;
    private String serverName;
    private int serverPort;
    private final static ExecutorService executor = Executors.newSingleThreadExecutor();

    private final static Logger logger = Logger.getLogger(ClientManager.class.getName());
    private final static String CLIENT_INFO = "CLIENT -> ";
    private final static String CONNECTING_INFO = " STA CERCANDO DI STABILIRE UNA CONNESSIONE VERSO IL SERVER \n";
    private final static String CONNECTION_ESTABLISHED_INFO = " CONNESSIONE AVVENUTA VERSO IL SERVER ";
    private final static String PORT_INFO = " ALLA PORTA ";
    private final static String SERVICE_INTERRUPTED = " STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";

    /**
     * Costruttore della classe ClientManager.
     *
     * @param serverName Nome del Server a cui connettersi
     * @param serverPort Porta del Server a cui connettersi
     */

    public ClientManager(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    /**
     * Permette di effettuare un tentativo di connessione al Server, i cui dati sono stati specificati
     * in fase di costruzione di questo oggetto.
     */

    public void attemptToConnect() {
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

    public InputStream getInputStream() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Permette di inviare un messaggio al Server su un altro Thread.
     * @param message Messaggio da inviare
     * @param <T> Tipo di messaggio da inviare (tutti devono implementare {@link Message}
     */

    public <T extends Message> void sendMessage(T message){
        executor.submit(new RequestSender<>(socket, message, logger));
    }

    /**
     * Permette di rimanere in ascolto di un messaggio qualsiasi su un altro Thread.
     *
     * @param <T> Tipo di messaggio da inviare (tutti devono implementare {@link Message}
     * @return Messaggio ricevuto
     */

    public <T extends Message> T listenForAMessage() {
        T message = null;
        try {
            message = executor.submit(new RequestHandler<T>(socket, logger)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return message;
    }
}
