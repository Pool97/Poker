package server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static view.ClientFrame.txtLogCl;
import static view.ServerFrame.txtLogSv;

public class EchoThread implements Runnable {

	private Socket socket;
	private ObjectOutputStream output_stream;
	private ObjectInputStream input_stream;
	private String id;
	private final Logger logger = Logger.getLogger(EchoThread.class.getName());
	private static List<EchoThread> instances = new ArrayList<EchoThread>();

	
	public EchoThread(Socket clientSocket, String id) {
		this.socket = clientSocket;	
		this.id = id;
	}

	public synchronized void addInstance() {

		instances.add(this);
    }

    public synchronized void removeInstance() {
        instances.remove(this);
    }
	
	public synchronized void run() {
		try {
			createStreams();
			initProcessing();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	private void createStreams () throws IOException {
		output_stream = new ObjectOutputStream(socket.getOutputStream());
		output_stream.flush();
		input_stream = new ObjectInputStream(socket.getInputStream());
		logger.info("SERVER -> STREAM " + id + " CREATI");
		updateLogCl("SERVER -> STREAM " + id  + " CREATI ");
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s);
	    } catch(NumberFormatException e) {
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	private void initProcessing() throws IOException {
		addInstance();
		String client_msg=" ";
		int  client_bet=0;
		String btn_status = " ";
		sendDataToClient("SERVER -> Digita BYE per terminare ...");
		//updateLogCl("SERVER -> Digita BYE per terminare ...");
		do {
			try {

				id = ((String) input_stream.readObject());
				//System.out.println(id);
				client_msg = ((String) input_stream.readObject());
				//System.out.println(client_msg);

				if(isInteger(client_msg) == true){
					btn_status = ((String) input_stream.readObject());
					System.out.println(btn_status);
					client_bet = Integer.parseInt(client_msg);
				}
				else{
					client_bet = 0;
					btn_status = " ";
					client_msg = client_msg.toUpperCase();
				}




				
				if (client_msg.startsWith("ALL ")) {
					sendToAll(client_msg, this);
					System.out.println("CLIENT " + id + " -> "+ client_msg);
					updateLogSv(("CLIENT " + id + " -> "+ client_msg));
				}
				else if (client_msg.equals(".")) {
					System.out.println("CLIENT " + id + " -> "+ client_msg);
					updateLogSv("CLIENT " + id + " -> "+ client_msg);
					//per gestire i messaggi ALL
				}
				else if (client_msg.startsWith("LIST")) {
					String list = "";
					for (EchoThread h : instances) {
						list = list + h.getId() + " ";
					}
					System.out.println("CLIENT " + id + " -> "+ list);
					updateLogSv("CLIENT " + id + " -> "+ list);
					sendDataToClient("CLIENTS ID LIST: -> "+ list);
					//updateLogCl("CLIENTS ID LIST: -> "+ list);
				}
				else {
					
					if(client_bet != 0 && !btn_status.equals(" "))
					{
						System.out.println("CLIENT " + id + " " + btn_status +" -> " + client_bet);
						updateLogSv("CLIENT " + id + " " + btn_status +" -> " + client_bet);
						sendDataToClient("SERVER ECHO " + btn_status + ": -> "+client_bet);
					}else if(!client_msg.equals(" ")) {
						System.out.println("CLIENT " + id + " -> "+ client_msg);
						updateLogSv("CLIENT " + id + " -> "+ client_msg);
						sendDataToClient("SERVER ECHO: -> "+client_msg);
					}
					
					//updateLogCl("SERVER ECHO: -> "+ client_msg);
				}			
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}		
		}
		while (!client_msg.contains("BYE"));
	}
	
	private String getId() {
		return id;
	}

	private void sendDataToClient(String msg) throws IOException {
		output_stream.writeObject(msg);
		output_stream.flush();
	}
	
	public synchronized void sendToAll(String msg, EchoThread me) throws IOException {
        for (EchoThread h : instances) {
            h.sendDataToClient("SERVER ECHO ALL: -> " + msg);
            //updateLogCl("SERVER ECHO ALL: -> " + msg);
            
        }
    }
	
	private void close() throws IOException {
		System.out.println("SERVER -> CHIUSURA CONNESSIONE SOCKET " + id);
		updateLogSv("SERVER -> CHIUSURA CONNESSIONE SOCKET " + id);
		logger.info("SERVER -> CHIUSURA CONNESSIONE SOCKET " + id);
		if(output_stream != null && input_stream != null && socket != null) {
			output_stream.close();
			input_stream.close();
			removeInstance();
			socket.close();
		}
	}
	private void updateLogSv(String text) {
		txtLogSv.append(String.format("%4d - %s\n", txtLogSv.getLineCount(), text));
	}
	
	private void updateLogCl(String text) {
		txtLogCl.append(String.format("%4d - %s\n", txtLogCl.getLineCount(), text));
	}

	private Socket getSocket(){
		return socket;
	}

	public static List<Socket> getAllSockets(){
		return instances.stream().map(echoThread -> echoThread.getSocket()).collect(Collectors.toList());
	}
}
