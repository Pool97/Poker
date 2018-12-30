package client.net;

import interfaces.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
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
    private String serverName;
    private int serverPort;
    public final static Logger logger = Logger.getLogger(Client.class.getName());
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;

    private final static String CLIENT_INFO = "CLIENT -> ";
    private final static String CONNECTING_INFO = " STA CERCANDO DI STABILIRE UNA CONNESSIONE VERSO IL SERVER \n";
    private final static String CONNECTION_ESTABLISHED_INFO = " CONNESSIONE AVVENUTA VERSO IL SERVER ";
    private final static String PORT_INFO = " ALLA PORTA ";

    public void setParameters(String serverName, int serverPort){
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void attemptToConnect() throws IOException{
        logger.info(CLIENT_INFO + CONNECTING_INFO);
        socket = new Socket();
        socket.connect(new InetSocketAddress(serverName, serverPort), 3000);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());
        logger.info(CLIENT_INFO + CONNECTION_ESTABLISHED_INFO + serverName + PORT_INFO +  serverPort + "\n");
    }

    public Object readMessage() throws IOException, ClassNotFoundException {
        return inputStream.readObject();
    }

    public Object writeMessage(Event message){
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        }
         catch (IOException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return null;
    }

    public void close() {
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
