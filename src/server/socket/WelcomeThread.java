package server.socket;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * Thread di benvenuto del ServerSocket.
 *
 * @author Nipuna Perera
 * @author Roberto Poletti
 * @since 1.0
 */

public class WelcomeThread implements Callable<Integer> {
    private Socket clientSocket;
    private ObjectInputStream input_stream;
    private ObjectOutputStream output_stream;
    private String nickname;
    private final Logger logger = Logger.getLogger(ServerSocketManager.class.getName());

    private final static String CLOSING_STREAMS_INFO = " CHIUSURA DEGLI STREAMS \n";
    private final static String SERVER_INFO = "SERVER -> ";
    private final static String STREAMS_CREATION_INFO = " STREAMS CREATI \n";
    private final static String SERVICE_INTERRUPTED = " STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";

    public WelcomeThread(Socket client){
        this.clientSocket = client;

    }

    @Override
    public Integer call(){
        createStreams();
        int maxPlayers = initProcessing();
        close();
        return maxPlayers;
    }

    private int initProcessing(){
        int playersNumber = 0;

        try {
            nickname = (String)input_stream.readObject();
            playersNumber = input_stream.readInt();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.info(SERVER_INFO + SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }

        return playersNumber;
    }

    private void close(){
        if(output_stream != null && input_stream != null && clientSocket != null) {

            try {
                output_stream.close();
                input_stream.close();
                logger.info(SERVER_INFO + CLOSING_STREAMS_INFO);
            } catch (IOException e) {
                e.printStackTrace();
                logger.info(SERVER_INFO + SERVICE_INTERRUPTED);
                Thread.currentThread().interrupt();
            }

        }
    }

    private void createStreams(){
        try {
            output_stream = new ObjectOutputStream(clientSocket.getOutputStream());
            output_stream.flush();
            input_stream = new ObjectInputStream(clientSocket.getInputStream());
            logger.info(SERVER_INFO + STREAMS_CREATION_INFO);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(SERVER_INFO + SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
    }
}
