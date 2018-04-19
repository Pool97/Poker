package server.socket;

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
	private Socket socket;
	private final Logger logger = Logger.getLogger(ServerSocketManager.class.getName());
	private CountDownLatch playersSignal;
	private CountDownLatch clientCreatorSignal;
	private ArrayList<Socket> clients;
	private int totalPlayers;

    private final static String LISTEN_FOR_CLIENTS_INFO = "SERVER -> IN ATTESA DI CONNESSIONI... \n";
    private final static String CLIENT_CONNECTED_INFO = "SERVER -> CONNESSO CON";
    private final static String REMOTE_PORT_INFO = " ALLA PORTA REMOTA ";
    private final static String LOCAL_PORT_INFO = " E ALLA PORTA LOCALE ";
    private final static String SERVER_ERROR = "I/O ERROR. ";
    private final static String WAITING_FOR_INFO= "SERVER -> IN ATTESA DI ";
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

	public ServerSocketManager(CountDownLatch clientCreatorSignal){
		this.clientCreatorSignal = clientCreatorSignal;
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
     * Permette di rimanere in ascolto delle richieste di connessione da parte dei giocatori.
	 * Il primo Client deve essere necessariamente il creatore della stanza.
	 * Per ogni giocatore viene memorizzata la reference al suo Socket.
     * Si ricorda che la massima lunghezza della connection queue è pari a 8.
     */

	private void listen(){
        logger.info(LISTEN_FOR_CLIENTS_INFO);

        try {
			socket = serverSocket.accept();
			logger.info(CLIENT_CONNECTED_INFO + socket.getInetAddress() + REMOTE_PORT_INFO + socket.getPort() + LOCAL_PORT_INFO + socket.getLocalPort() + "\n");

			if(playersSignal != null)
				playersSignal.countDown();
			else{
				totalPlayers = new WelcomeThread(socket).call();
				logger.info(WAITING_FOR_INFO + totalPlayers + PLAYERS);
				clientCreatorSignal.countDown();
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

	public void setCountdownForPlayers(CountDownLatch waitingPlayers){
		this.playersSignal = waitingPlayers;
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
}

