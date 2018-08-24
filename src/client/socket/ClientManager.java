package client.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Classe che permette di gestire l'infrastruttura di rete lato Client.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class ClientManager {
    private String serverName;
    private int serverPort;
    public final static Logger logger = Logger.getLogger(ClientManager.class.getName());
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;
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
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            logger.info(CLIENT_INFO + CONNECTION_ESTABLISHED_INFO + serverName + PORT_INFO +  serverPort + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(CLIENT_INFO  + SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
