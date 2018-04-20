package server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Qualsiasi Client che si collega dopo il creatore della stanza.
 *
 * @author Nipuna Perera
 * @author Roberto Poletti
 * @since 1.0
 */

public class ClientSocket implements Runnable{
    private Socket socket;
    private String nickname;
    private String serverName;
    private int serverPort;
    private ObjectOutputStream output_stream;
    private ObjectInputStream input_stream;
    private static Logger logger = Logger.getLogger(ClientSocket.class.getName());

    private final static String CONNECTING_INFO = " STA CERCANDO DI STABILIRE UNA CONNESSIONE VERSO IL SERVER \n";
    private final static String CONNECTION_ESTABLISHED_INFO = " CONNESSIONE AVVENUTA VERSO IL SERVER ";
    private final static String CLOSING_STREAMS_INFO = " CHIUSURA DEGLI STREAMS \n";
    private final static String STREAMS_CREATION_INFO = " STREAMS CREATI \n";
    private final static String SERVICE_INTERRUPTED = " STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";
    private final static String CLIENT_INFO = "CLIENT -> ";
    private final static String PORT_INFO = " ALLA PORTA ";

    /**
     * Costruttore della classe ClientSocket.
     * Per collegarsi alla partita sono necessari solo gli argomenti in ingresso al costruttore.
     *
     * @param nickname Nickname del Player
     * @param serverName Nome del Server
     * @param serverPort Numero della porta del Server
     */

    public ClientSocket(String nickname, String serverName, int serverPort) {
        this.nickname = nickname;
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        attemptToConnect();
        createStreams();
        close();
    }

    private void attemptToConnect(){
        logger.info(CLIENT_INFO + nickname + CONNECTING_INFO);
        try {
            socket = new Socket(serverName, serverPort);
            logger.info(CLIENT_INFO + nickname + CONNECTION_ESTABLISHED_INFO + serverName + PORT_INFO +  serverPort + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(CLIENT_INFO + nickname + SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
    }

    private void close(){
        logger.info(CLIENT_INFO + nickname + CLOSING_STREAMS_INFO);

        if(output_stream != null && input_stream != null) {
            try {
                output_stream.close();
                input_stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info(CLIENT_INFO + nickname + SERVICE_INTERRUPTED);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void createStreams(){
        try {
            output_stream = new ObjectOutputStream(socket.getOutputStream());
            output_stream.flush();
            input_stream = new ObjectInputStream(socket.getInputStream());
            logger.info(CLIENT_INFO + nickname + STREAMS_CREATION_INFO);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(CLIENT_INFO + nickname + SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String [] args){
        new Thread(new ClientSocket("Tunsi97", "localhost", 4040)).start();
        new Thread(new ClientSocket("Perry97", "localhost", 4040)).start();
    }
}
