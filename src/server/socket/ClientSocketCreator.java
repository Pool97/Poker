package server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Il Client che è incaricato di creare la stanza necessaria per la partita.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class ClientSocketCreator implements Runnable{
    private String nickname;
    private int totalPlayers;

    private Socket socket;
    private String serverName;
    private int serverPort;
    private ObjectOutputStream output_stream;
    private ObjectInputStream input_stream;

    private static Logger logger = Logger.getLogger(ClientSocketCreator.class.getName());

    private final static String CLIENT_CREATOR_INFO = "CLIENT CREATOR ->";
    private final static String CONNECTING_INFO = " STA CERCANDO DI STABILIRE UNA CONNESSIONE VERSO IL SERVER ";
    private final static String CONNECTION_ESTABLISHED_INFO = " CONNESSIONE AVVENUTA VERSO IL SERVER ";
    private final static String CLOSING_STREAMS_INFO = " CHIUSURA DEGLI STREAMS \n";
    private final static String STREAMS_CREATION_INFO = " STREAMS CREATI \n";
    private final static String MESSAGE_SENT_INFO = " INFORMATO IL SERVER SUL NUMERO TOTALE DEI GIOCATORI \n";
    private final static String SERVICE_INTERRUPTED = " STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";
    private final static String PORT_INFO = " ALLA PORTA ";

    /**
     * Costruttore della classe ClientSocketCreator.
     * Per creare la partita sono necessari solo gli argomenti in ingresso al costruttore.
     *
     * @param nickname Nickname del Player che crea la stanza
     * @param totalPlayers Numero dei partecipanti alla partita
     * @param serverName Nome del Server a cui ci si deve connettere
     * @param serverPort Numero della porta del Server
     */

    public ClientSocketCreator(String nickname, int totalPlayers, String serverName, int serverPort){
        this.nickname = nickname;
        this.totalPlayers = totalPlayers;
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        attemptToConnect();
        createStreams();
        initProcessing();
        closeStreams();
    }

    private void attemptToConnect(){
        logger.info(CLIENT_CREATOR_INFO + nickname + CONNECTING_INFO + serverName + PORT_INFO + serverPort + "\n");
        try {
            socket = new Socket(serverName, serverPort);
            logger.info(CLIENT_CREATOR_INFO + nickname + CONNECTION_ESTABLISHED_INFO + serverName + PORT_INFO + serverPort + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            interruptService();
        }
    }

    /**
     * Permette al creatore della stanza di identificarsi presso il server e di informarlo sul numero massimo
     * di partecipanti alla partita.
     */

    private void initProcessing(){
        try {
            output_stream.writeObject(nickname);
            output_stream.writeInt(totalPlayers);
            logger.info(CLIENT_CREATOR_INFO + nickname + MESSAGE_SENT_INFO);
        } catch (IOException e) {
            e.printStackTrace();
            interruptService();
        }
    }

    private void createStreams(){
        try {
            output_stream = new ObjectOutputStream(socket.getOutputStream());
            output_stream.flush();
            input_stream = new ObjectInputStream(socket.getInputStream());
            logger.info(CLIENT_CREATOR_INFO + nickname + STREAMS_CREATION_INFO);
        } catch (IOException e) {
            e.printStackTrace();
            interruptService();
        }
    }

    private void closeStreams(){
        if(output_stream != null && input_stream != null) {
            try {
                logger.info(CLIENT_CREATOR_INFO + nickname + CLOSING_STREAMS_INFO);
                output_stream.close();
                input_stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                interruptService();
            }
        }
    }

    private void interruptService(){
        logger.info(CLIENT_CREATOR_INFO + nickname + SERVICE_INTERRUPTED);
        Thread.currentThread().interrupt();
    }

    /**
     * Restituisce il Socket relativo al creatore della stanza.
     * Da notare che il creatore è anch'esso facente parte della partita, perciò non si vuole perdere la reference
     * al Socket nel momento in cui il Thread si esaurisce, poichè altrimenti rimarrebbe una half-connection
     * e l'utente non potrebbe dialogare con gli altri Players nel corso di tutta la partita.
     *
     * @return Socket del creatore della stanza
     */

    public Socket getSocket(){
        return socket;
    }

    public static void main(String [] args){
        new Thread(new ClientSocketCreator("Pool97", 2, "localhost", 4040)).start();
    }
}
