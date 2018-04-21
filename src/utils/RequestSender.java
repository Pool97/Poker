package utils;

import interfaces.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * RequestSender è la classe duale di {@link RequestHandler}, permette di inviare un messaggio al Server/Client.
 * Per rendere il processo efficiente, è utile che venga eseguito su un Thread diverso da quello chiamante.
 * RequestSender è progettata per mantenere astrattezza. Poichè lo Strategy Pattern, in questo specifico caso, risultava
 * essere poco utile, l'astrattezza viene mantenuta con le Generics, in modo che si possa rimanere in ascolto e processare
 * qualsiasi messaggio che aderisce all'interfaccia {@link Message}.
 *
 * @param <T> Classe che implementa l'interfaccia {@link Message}.
 */

public class RequestSender<T extends Message> implements Runnable{
    private Socket socket;
    private T message;
    private ObjectOutputStream output_stream;
    private ObjectInputStream input_stream;
    private Logger logger;

    private final static String STREAMS_CREATION_INFO = "STREAMS CREATI \n";
    private final static String SERVICE_INTERRUPTED = "STO INTERROMPENDO IL SERVIZIO PER UN ERRORE I/O \n";

    /**
     * Costruttore della classe RequestSender.
     *
     * @param socket Il Socket utilizzato per inviare il messaggio al Client/Server
     * @param message Il messaggio da inviare.
     * @param logger Il logger utilizzato per mostrare informazioni di sistema.
     */

    public RequestSender(Socket socket, T message, Logger logger){
        this.socket = socket;
        this.message = message;
        this.logger = logger;
    }

    /**
     * {@link Runnable#run}
     */

    @Override
    public void run() {
        createStreams();
        try {
            output_stream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createStreams(){
        try {
            output_stream = new ObjectOutputStream(socket.getOutputStream());
            output_stream.flush();
            input_stream = new ObjectInputStream(socket.getInputStream());
            logger.info(STREAMS_CREATION_INFO);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(SERVICE_INTERRUPTED);
            Thread.currentThread().interrupt();
        }
    }
}
