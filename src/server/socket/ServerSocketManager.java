package server.socket;

import server.interfaces.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Classe che permette di gestire l'intera infrastruttura Server. È la principale responsabile del collegamento tra i Client e
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
	private int totalPlayers;

	private final Logger logger = Logger.getLogger(ServerSocketManager.class.getName());
	public final static String SERVER_INFO = "SERVER -> ";
    private final static String LISTEN_FOR_CLIENTS_INFO = "IN ATTESA DI CONNESSIONI... \n";
    private final static String CLIENT_CONNECTED_INFO = "CONNESSO CON";
    private final static String REMOTE_PORT_INFO = " ALLA PORTA REMOTA ";
    private final static String LOCAL_PORT_INFO = " E ALLA PORTA LOCALE ";
    private final static String SERVER_ERROR = "I/O ERROR. ";
    private final static String WAITING_FOR_INFO= "IN ATTESA DI ALTRI ";
    private final static String PLAYERS = " GIOCATORI \n";
	private final static String SERVER_SHUTDOWN_INFO = "SHUTTING DOWN THE SERVER...\n";
    private final static String PLAYER_ADDED = "GIOCATORE AGGIUNTO ALLA LISTA PER LA PARTITA IMMINENTE... \n";
    private final static int SERVER_PORT = 4040;
    private final static int MAX_CONNECTION_QUEUE_LENGTH = 8;

    /**
     * Costruttore vuoto di ServerSocketManager.
     * Viene istanziato l'ArrayList, che conterrà la lista dei Socket che permettono il contatto tra
     * i vari Client e il Server.
     * Viene inoltre effettuata un'istanza del ServerSocket.
     */

	public ServerSocketManager(CountDownLatch roomCreationSignal){
		this.roomCreationSignal = roomCreationSignal;
	    clients = new ArrayList<>();

		try {
			serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTION_QUEUE_LENGTH);
		}
		catch (IOException e) {
			logger.log(Level.WARNING, SERVER_ERROR + SERVER_SHUTDOWN_INFO);
		}
	}

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
			logger.info(SERVER_INFO + CLIENT_CONNECTED_INFO + socket.getInetAddress() + REMOTE_PORT_INFO + socket.getPort() + LOCAL_PORT_INFO + socket.getLocalPort() + "\n");
			if(isRoomCreationSignalExpire()) {
				playersSignal.countDown();
				WelcomePlayerMessage message = listenForAMessage(socket);
			} else{
				WelcomeCreatorMessage message = listenForAMessage(socket);
				totalPlayers = message.getMaxPlayers();
				logger.info(SERVER_INFO + WAITING_FOR_INFO + totalPlayers + PLAYERS);
				roomCreationSignal.countDown();
			}

			clients.add(socket);
			logger.info(PLAYER_ADDED);

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
	 * Restituisce il numero totale dei giocatori previsti dal match.
	 * Si esclude dalla lista il creatore della stanza.
	 * @return Numero totale dei giocatori del match.
	 */

	public int getTotalPlayers(){
		return totalPlayers;
	}

	/**
	 * Permette di restituire le reference ai Socket di tutti i giocatori del match.
	 * @return Lista contenente i Socket dei vari giocatori.
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
		return new RequestHandler<T>(socket).call();
	}
}

