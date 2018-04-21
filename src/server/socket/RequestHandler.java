package server.socket;

import server.interfaces.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import static server.socket.ServerSocketManager.SERVER_INFO;

/**
 * RequestHandler è la classe che è incaricata di rimanere in attesa di un messaggio inviato da qualsiasi Client.
 * Per rendere il processo efficiente, è utile che venga eseguito su un Thread diverso da quello di accept.
 * RequestHandler è progettata per mantenere astrattezza. Poichè lo Strategy Pattern, in questo specifico caso, risultava
 * essere poco utile, l'astrattezza viene mantenuta con le Generics, in modo che si possa rimanere in ascolto e processare
 * qualsiasi messaggio che aderisce all'interfaccia {@link Message}.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 * @param <T> Classe che implementa l'interfaccia {@link Message}
 */

public class RequestHandler<T extends Message> implements Callable<T> {
    private Socket clientSocket;
    private ObjectInputStream input_stream;
    private ObjectOutputStream output_stream;
    private final Logger logger = Logger.getLogger(ServerSocketManager.class.getName());

    private final static String CLOSING_STREAMS_INFO = " CHIUSURA DEGLI STREAMS \n";
    private final static String STREAMS_CREATION_INFO = " STREAMS CREATI \n";
    private final static String SERVICE_INTERRUPTED = " STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";

    /**
     * Costruttore di RequestHandler.
     *
     * @param clientSocket Il Socket utilizzato per rimanere in ascolto del Client.
     */

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T call(){
        createStreams();
        T messageObject = null;
        try {
            messageObject = (T) input_stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.warning(SERVER_INFO + SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }finally {
            close();
        }

        return messageObject;
    }

    private void close(){
        if(output_stream != null && input_stream != null) {

            try {
                output_stream.close();
                input_stream.close();
                logger.info(SERVER_INFO + CLOSING_STREAMS_INFO);
            } catch (IOException e) {
                e.printStackTrace();
                logger.warning(SERVER_INFO + SERVICE_INTERRUPTED);
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
            logger.warning(SERVER_INFO + SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
    }
}
