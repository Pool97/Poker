package utils;

import interfaces.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * RequestHandler è la classe che è incaricata di rimanere in attesa di un messaggio inviato da Client o da Server.
 * Per rendere il processo efficiente, è utile che venga eseguito su un Thread diverso da quello chiamante.
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
    private Socket socket;
    private ObjectInputStream input_stream;
    private ObjectOutputStream output_stream;
    private Logger logger;

    private final static String STREAMS_CREATION_INFO = "STREAMS CREATI \n";
    private final static String SERVICE_INTERRUPTED = " STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";

    /**
     * Costruttore di RequestHandler.
     *
     * @param socket Il Socket utilizzato per rimanere in ascolto del Server/Client.
     * @param logger Il logger utilizzato per mostrare informazioni di sistema.
     */

    public RequestHandler(Socket socket, Logger logger) {
        this.socket = socket;
        this.logger = logger;
    }

    /**
     * {@link Callable#call}
     */

    @Override
    @SuppressWarnings("unchecked")
    public T call(){
        createStreams();
        T messageObject = null;
        try {
            messageObject = (T) input_stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.warning(SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
        return messageObject;
    }

    private void createStreams(){
        try {
            output_stream = new ObjectOutputStream(socket.getOutputStream());
            output_stream.flush();
            input_stream = new ObjectInputStream(socket.getInputStream());
            logger.info(STREAMS_CREATION_INFO);
        } catch (IOException e) {
            e.printStackTrace();
            logger.warning(SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
    }
}
