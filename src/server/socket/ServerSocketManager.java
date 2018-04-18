package server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static view.AvatarFrame.txtInput;
import static view.ServerFrame.txtLogSv;

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
	private final ExecutorService executor = Executors.newFixedThreadPool(20);
	private ArrayList<Socket> clients;

    private final static String LISTEN_FOR_CLIENTS_INFO = "SERVER -> IN ATTESA DI CONNESSIONI...";
    private final static String CLIENT_CONNECTED_INFO = "SERVER -> CONNESSO CON ";
    private final static String REMOTE_PORT_INFO = " ALLA PORTA REMOTA ";
    private final static String LOCAL_PORT_INFO = " E ALLA PORTA LOCALE ";
    private final static String SERVER_SHUTDOWN_INFO = "I/O ERROR. SHUTTING DOWN THE SERVER...";

    private final static int SERVER_PORT = 4040;
    private final static int MAX_CONNECTION_QUEUE_LENGTH = 8;

    /**
     * Costruttore vuoto di ServerSocketManager.
     * Viene istanziato l'ArrayList, che conterrà la lista dei Socket che permettono il contatto tra
     * i vari Client e il Server.
     * Viene inoltre effettuata un'istanza del ServerSocket.
     */

	public ServerSocketManager(){
	    clients = new ArrayList<>();

		try {
			serverSocket = new ServerSocket(SERVER_PORT, MAX_CONNECTION_QUEUE_LENGTH);
		} catch (IOException e) {
            logger.log(Level.WARNING, SERVER_SHUTDOWN_INFO);
		}
	}

    /**
     * Permette di avviare il funzionamento del Server nell'istante immediatamente successivo all'avvio
     * del Thread.
     */

	public void run() {
		do {
			try{
			    listen();
			    executor.submit(new EchoThread(socket, txtInput.getText()));
			}catch (IOException e) {
				logger.log(Level.WARNING, SERVER_SHUTDOWN_INFO);
				Thread.currentThread().interrupt();
			}
		}while(true);
	}

    /**
     * Permette di rimanere in ascolto di eventuali richieste di connessione da parte dei Client.
     * Si ricorda che la massima lunghezza della coda è pari a 8.
     * @throws IOException
     */

	private void listen() throws IOException {
        logger.info(LISTEN_FOR_CLIENTS_INFO);
        socket = serverSocket.accept();
        logger.info(CLIENT_CONNECTED_INFO + socket.getInetAddress() + REMOTE_PORT_INFO + socket.getPort() + LOCAL_PORT_INFO + socket.getLocalPort());
		clients.add(socket);
	}
	
	private void updateLog(String text) {
		txtLogSv.append(String.format("%4d - %s\n", txtLogSv.getLineCount(), text));
	}
}

