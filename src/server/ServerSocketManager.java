package server;

import client.messages.CreationRoomMessage;
import client.messages.WelcomePlayerMessage;
import interfaces.Message;
import utils.RequestHandler;
import utils.RequestSender;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Classe che permette di gestire l'intera infrastruttura di rete lato Server. È la principale responsabile del collegamento tra i Client e
 * il Server.
 *
 * @author Nipuna Perera
 * @author Roberto Poletti
 * @since 1.0
 */

public class ServerSocketManager implements Runnable {
    private ServerSocket serverSocket;
	private CountDownLatch playersSignal;
	private CountDownLatch roomCreationSignal;
	private ArrayList<Socket> clients;
    private ArrayList<WelcomePlayerMessage> playersInfo;
	private CreationRoomMessage matchConfig;

    private final static ExecutorService executor = Executors.newSingleThreadExecutor();
	private final Logger logger = Logger.getLogger(ServerSocketManager.class.getName());

	public final static String SERVER_INFO = "SERVER -> ";
    public final static int SERVER_PORT = 4040;
    public final static int MAX_CONNECTION_QUEUE_LENGTH = 8;

    private final static String LISTEN_FOR_CLIENTS_INFO = "IN ATTESA DI CONNESSIONI... \n";
    private final static String CLIENT_CONNECTED_INFO = "CONNESSO CON";
    private final static String REMOTE_PORT_INFO = " ALLA PORTA REMOTA ";
    private final static String LOCAL_PORT_INFO = " E ALLA PORTA LOCALE ";
    private final static String SERVER_ERROR = "I/O ERROR. ";
    private final static String WAITING_FOR_INFO= "IN ATTESA DI ALTRI ";
    private final static String PLAYERS = " GIOCATORI \n";
	private final static String SERVER_SHUTDOWN_INFO = "SHUTTING DOWN THE SERVER...\n";
    private final static String PLAYER_ADDED = "GIOCATORE AGGIUNTO ALLA LISTA PER LA PARTITA IMMINENTE... \n";

    /**
     * Costruttore vuoto di ServerSocketManager.
     * Viene istanziato l'ArrayList, che conterrà la lista dei Socket che permettono il contatto tra
     * i vari Client e il Server.
     * Viene inoltre effettuata un'istanza del ServerSocket.
     */

	public ServerSocketManager(CountDownLatch roomCreationSignal){
		this.roomCreationSignal = roomCreationSignal;
	    clients = new ArrayList<>();
	    playersInfo = new ArrayList<>();

		try {
			serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTION_QUEUE_LENGTH);
		}
		catch (IOException e) {
			logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
		}
	}

	/**
	 * {@link Runnable#run()}
	 */

	public void run() {
		do
			listen();
		while(true);
	}

    /**
     * Permette di rimanere in ascolto delle richieste iniziali di connessione da parte dei giocatori.
	 * Il primo Client deve essere necessariamente il creatore della stanza.
	 * Per ogni giocatore viene memorizzata la reference al suo Socket.
     * Si ricorda che la massima lunghezza della connection queue è pari a 8.
     */

	private void listen(){
        logger.info(SERVER_INFO + LISTEN_FOR_CLIENTS_INFO);

        try {
			Socket socket = serverSocket.accept();
            clients.add(socket);
			logger.info(SERVER_INFO + CLIENT_CONNECTED_INFO + socket.getInetAddress() + REMOTE_PORT_INFO + socket.getPort() + LOCAL_PORT_INFO + socket.getLocalPort() + "\n");
            logger.info(PLAYER_ADDED);

            if(isRoomCreationSignalExpire()) {
				playersInfo.add(listenForAMessage(socket));
				playersSignal.countDown();
			}else{
				matchConfig = listenForAMessage(socket);
				playersInfo.add(listenForAMessage(socket));
				logger.info(SERVER_INFO + WAITING_FOR_INFO + (matchConfig.getMaxPlayers() - 1) + PLAYERS);
				roomCreationSignal.countDown();
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Permette di settare un Countdown per l'attesa di tutti i giocatori, eccetto il creatore della stanza
	 * che ne ha uno tutto suo.
	 * Per ogni giocatore connesso viene decrementato il Countdown, una volta che tutti i giocatori si sono
	 * connessi il countdown raggiunge lo zero e viene informato lo StateManager che ha la possibilità di far
	 * partire il match.
	 *
	 * @param waitingPlayers Segnale di Countdown per l'attesa dei giocatori del Match.
	 */

	public void setCountdownForClients(CountDownLatch waitingPlayers){
		this.playersSignal = waitingPlayers;
	}

	/**
	 * Permette di settare un CountDown per l'attesa del creatore della stanza.
	 * Una volta che il creatore si è connesso al Server, viene configurato il Match secondo i parametri scelti da esso
	 * e mette il ServerSocketManager in condizione di aspettare tutte le altre connessioni dei players.
	 *
	 * @param roomCreationSignal Segnale di avvenuta ricezione del creatore della stanza.
	 */

	public void setCountdownForCreatorClient(CountDownLatch roomCreationSignal){
		this.roomCreationSignal = roomCreationSignal;
	}

	/**
	 * Permette di determinare se la connessione con il creatore della stanza è avvenuta oppure no.
	 * @return True se è già avvenuta, False altrimenti.
	 */

	public boolean isRoomCreationSignalExpire(){
		return roomCreationSignal.getCount() == 0;
	}

	/**
	 * Restituisce la configurazione del Match scelta dal creatore della stanza.
	 * @return Configurazione del Match.
	 */

	public CreationRoomMessage getMatchConfiguration(){
	    return matchConfig;
    }

	/**
	 * Permette di restituire le informazioni impostate dai Players in fase pre-partita.
	 * @return Informazioni iniziali provenienti dai Players.
	 */

	public ArrayList<WelcomePlayerMessage> getPlayersInformation(){
	    return playersInfo;
    }

	/**
	 * Permette di ritornare le connessioni con tutti i Clients.
	 * @return ClientSockets
	 */

	public ArrayList<Socket> getAllClientsConnection(){
		return clients;
	}

	/**
	 * Permette di rimanere in ascolto di un qualsiasi messaggio informativo inviato da un qualsiasi Client.
	 *
	 * @param socket Socket relativo al Player che si vuole ascoltare.
	 * @param <T> Il messaggio da ascoltare dovrà essere un qualsiasi tipo di messaggio conforme all'interfaccia Message.
	 * @return Il messaggio inviato dal Client.
	 */

	public <T extends Message> T listenForAMessage(Socket socket){
	    T message = null;
        try {
            message = executor.submit(new RequestHandler<T>(socket,logger)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return message;
    }

	/**
	 * Permette di inviare un messaggio a un Client su un altro Thread.
	 * @param socket Connessione con il Client.
	 * @param message Messaggio da inviare
	 * @param <T> Tipo di messaggio da inviare (tutti devono implementare {@link Message}
	 */

	public <T extends Message> void sendMessage(Socket socket, T message){
		executor.submit(new RequestSender<>(socket, message, logger));
	}

	/**
	 * Versione di {@link ServerSocketManager#sendMessage(Socket, Message)} temporizzata.
	 * Questa si differenzia dalla sopraccitata per il fatto che il Thread chiamante viene bloccato
	 * in attesa che il messaggio sia andato a buon fine.
	 *
	 * @param socket Connessione con il Client.
	 * @param waiter Temporizzatore
	 * @param message Messaggio da inviare
	 * @param <T> Tipo di messaggio da inviare (tutti devono implementare {@link Message}
	 */

	public <T extends Message> void sendMessage(Socket socket, CountDownLatch waiter, T message){
		executor.submit(new RequestSender<>(socket, message, logger));
		waiter.countDown();
	}

}

