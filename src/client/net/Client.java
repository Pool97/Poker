package client.net;

import server.events.EventsContainer;

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

public class Client {
    private static Client istanza = null;
    private String serverName;
    private int serverPort;
    public final static Logger logger = Logger.getLogger(Client.class.getName());
    private String nickname;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;

    private final static String CLIENT_INFO = "CLIENT -> ";
    private final static String CONNECTING_INFO = " STA CERCANDO DI STABILIRE UNA CONNESSIONE VERSO IL SERVER \n";
    private final static String CONNECTION_ESTABLISHED_INFO = " CONNESSIONE AVVENUTA VERSO IL SERVER ";
    private final static String PORT_INFO = " ALLA PORTA ";
    private final static String SERVICE_INTERRUPTED = " STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";

    public static Client getInstance() {
        if(istanza == null)
            istanza = new Client();
        return istanza;
    }

    public void setParameters(String serverName, int serverPort){
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void setNickname(String nickname){
        if(this.nickname == null)
            this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

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

    public Object readMessage(){
        try {
            return inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object writeMessage(EventsContainer message){
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        }
         catch (IOException e) {
            //Client.logger.finer(WRITE_ERROR);
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return null;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
