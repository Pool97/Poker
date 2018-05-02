package server.socket;

import interfaces.Message;
import server.model.PlayerModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

/**
 * Classe che gestisce la connessione con il Player, lato server.
 *
 * @author Roberto Poletti
 * @author Nipuna Perera
 * @since 1.0
 */

public class Player {
    private Socket socket;
    private PlayerModel playerModel;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    /**
     * Costruttore della classe Player
     *
     * @param socket      Connessione relativa al Player
     * @param playerModel Model relativo al Player
     */

    public Player(Socket socket, PlayerModel playerModel) {
        this.socket = socket;
        this.playerModel = playerModel;
        createStreams();
    }

    /**
     * Costruttore della classe Player
     *
     * @param socket Connessione relativa al Player
     */

    public Player(Socket socket) {
        this.socket = socket;
        createStreams();
    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    public void setPlayerModel(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Permette di creare gli stream per l'invio e la ricezione di oggetti serializzati.
     * Importantissimo mantenere queste come istanze uniche lato Server, poichè altrimenti si potrebbero
     * verificare malfunzionamenti con l'uso dei socket.
     */

    private void createStreams() {
        try {
            outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.flush();
            inStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Permette di inviare un messaggio {@link Message} al Client.
     *
     * @param executorService ExecutorService incaricato di eseguire la Task su un altro Thread
     * @param message         Messaggio da inviare al Client
     * @param <T>             Messaggio che implementa l'interfaccia {@link Message}.
     */

    public <T extends Message> void sendMessage(ExecutorService executorService, T message) {
        try {
            executorService.submit(new RequestSender<>(message)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permette di ricevere un messaggio {@link Message} dal Client.
     *
     * @param executorService ExecutorService incaricato di eseguire la Task su un altro Thread
     * @param <T>             Messaggio che implementa l'interfaccia {@link Message}.
     * @return Messaggio ricevuto dal Client
     */

    public <T extends Message> T readMessage(ExecutorService executorService) {
        T message = null;
        try {
            message = executorService.submit(new RequestHandler<T>()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * RequestSender è la classe duale di {@link RequestHandler}, permette di inviare un messaggio attraverso il socket relativo al Player.
     * Per rendere il processo efficiente, è utile che venga eseguito su un Thread diverso da quello chiamante.
     * RequestSender è progettata per mantenere astrattezza attraverso le Generics, in modo che si possa
     * rimanere in ascolto e processare qualsiasi messaggio che aderisce all'interfaccia {@link Message}.
     *
     * @param <T> Classe che implementa l'interfaccia {@link Message}.
     */

    class RequestSender<T extends Message> implements Runnable {
        private T message;

        public RequestSender(T message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                outStream.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * RequestHandler è la classe duale di {@link RequestSender} ed è incaricata di rimanere in attesa di un messaggio inviato attraverso il socket
     * dal Player.
     * Per rendere il processo efficiente, è utile che venga eseguito su un Thread diverso da quello chiamante.
     * RequestHandler è progettata per mantenere astrattezza attraverso le Generics, in modo che si possa rimanere in ascolto e processare
     * qualsiasi messaggio che aderisce all'interfaccia {@link Message}.
     *
     * @param <T> Classe che implementa l'interfaccia {@link Message}
     */

    class RequestHandler<T extends Message> implements Callable<T> {

        @Override
        public T call() {
            T messageObject = null;
            try {
                messageObject = (T) inStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            return messageObject;
        }
    }
}


